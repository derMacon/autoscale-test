import { AmqService } from './service/AmqService';
import { PersistenceService } from './service/PersistenceService';
import { WorkerService } from './service/WorkerService';
import { ElementExtractor } from './utils/ElementExtractor';
import { XsdChecker } from './utils/XsdChecker';

require('dotenv').config();

let workerService: WorkerService = new WorkerService(
	new XsdChecker(),
	new ElementExtractor(),
	new PersistenceService()
);

new AmqService(workerService).connectBroker();
 