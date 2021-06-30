import {PaymentMessage} from './PaymentMessage';


export class ResultWrapper {

    containerId: string | undefined;
    batchId: number | undefined;
    serviceName: string | undefined;
    extractedElement: string | undefined;
    sentTimestamp: Date | undefined;
    receivedTimestamp: Date | undefined;
    processedTimestamp: Date | undefined;
    content: string | undefined;

    constructor(
        payment: PaymentMessage
    ) {
        this.batchId = payment.batchId;
        this.content = payment.content;
        this.sentTimestamp = payment.sentTimestamp;
        this.receivedTimestamp = (new Date());
        this.serviceName = 'NODE';
    }

    appendContainerId(containerId: string): ResultWrapper {
        this.containerId = containerId;
        return this;
    }

    appendServiceName(serviceName: string): ResultWrapper {
        this.serviceName = serviceName;
        return this;
    }

    appendExtractedElem(extractedElem: string): ResultWrapper {
        this.extractedElement = extractedElem;
        return this;
    }

    appendSentTimestamp(sentTimestamp: Date): ResultWrapper {
        this.sentTimestamp = sentTimestamp;
        return this;
    }

    appendReceivedTimestamp(receivedTimestamp: Date): ResultWrapper {
        this.receivedTimestamp = receivedTimestamp;
        return this;
    }

    appendProcessedTimestamp(processedTimestamp: Date): ResultWrapper {
        this.processedTimestamp = processedTimestamp;
        return this;
    }

}