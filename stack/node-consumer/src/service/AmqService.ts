import {WorkerService} from './WorkerService';
import {ResultWrapper} from "../model/ResultWrapper";

const stompit = require('stompit');

/**
 * Service handling all broker communication
 */
export class AmqService {

    private workerService: WorkerService;

    private payQueueDestination: string;
    private ackQueueDestination: string;
    private dbQueueDestination: string;
    private connectOptions: any;

    constructor(workerService: WorkerService) {
        this.workerService = workerService;
        this.payQueueDestination = process.env.AMQ_NODE_QUEUE_NAME!;
        this.ackQueueDestination = process.env.AMQ_NODE_ACK_QUEUE!;
        this.dbQueueDestination = process.env.AMQ_CONSUMER_PERSISTENCE_QUEUE!

        this.connectOptions = {
            'host': process.env.AMQ_BROKER_HOSTNAME!,
            'port': +process.env.AMQ_STOMP_PORT!,
            'connectHeaders': {
                'host': '/',
                'login': process.env.AMQ_USER_NAME,
                'passcode': process.env.AMQ_USER_PASS,
                'heart-beat': '5000:5000'
            }
        }
    }

    connectBroker(containerId: string) {
        const that = this;

        console.log("connection options: ", this.connectOptions);

        stompit.connect(this.connectOptions, function (error: any, client: any) {
            if (error) {
                console.log(' - amq connection error: ' + error.message);
                setTimeout(() => that.connectBroker(containerId), 1000)
                return;
            }

            that.sendAcknowledgement(client, that.ackQueueDestination, containerId);


            const subscribeHeaders = {
                'destination': that.payQueueDestination,
                'ack': 'client-individual',
                'activemq.prefetchSize': 1
            };

            client.subscribe(subscribeHeaders, function (error: any, message: any) {

                if (error) {
                    console.log('subscribe error ' + error.message);
                    setTimeout(() => that.connectBroker(containerId), 1000)
                    return;
                }

                message.readString('utf-8', function (error: any, body: any) {
                    if (error) {
                        console.log('read message error ' + error.message);
                        return;
                    }

                    console.log("new message received");

                    let result: ResultWrapper | undefined = that.workerService.work(body);
                    if (result != undefined) {
                        const sendHeaders = {
                            'destination': that.dbQueueDestination,
                            'content-type': 'text/plain'
                        };

                        const frame = client.send(sendHeaders);
                        let json: string = JSON.stringify(result);
                        frame.write(json);
                        console.log(" -> sending msg to persistence queue")
                        frame.end()
                    } else {
                        console.log("result undefined")
                    }

                    AmqService.wait(3000)
                    client.ack(message);
                    // client.disconnect();
                });

            });

        });
    }

    sendAcknowledgement(client: any, ackQueueDestination: string, containerId: string) {
        const sendHeaders = {
            'destination': ackQueueDestination,
            'content-type': 'text/plain'
        };

        console.log("send headers: ", sendHeaders)
        console.log("containerId: ", containerId)

        const frame = client.send(sendHeaders);
        frame.write(containerId);
        console.log("sending acknowledgement")
        frame.end()
    }

    static wait(ms: any) {
        var start = new Date().getTime();
        var end = start;
        while (end < start + ms) {
            end = new Date().getTime();
        }
    }
}