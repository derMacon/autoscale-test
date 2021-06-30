import {AmqService} from './service/AmqService';
import {WorkerService} from './service/WorkerService';
import {ElementExtractor} from './utils/ElementExtractor';
import {XsdChecker} from './utils/XsdChecker';
import {IdGenerator} from './utils/IdGenerator';

require('dotenv').config();

let containerId: string = IdGenerator.generateContainerId();

let workerService: WorkerService = new WorkerService(
    new XsdChecker(),
    new ElementExtractor(),
    containerId
);

new AmqService(workerService).connectBroker(containerId);