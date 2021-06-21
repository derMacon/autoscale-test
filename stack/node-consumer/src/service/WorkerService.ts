import { ResultWrapper } from "../model/ResultWrapper";
import { ElementExtractor } from "../utils/ElementExtractor";
import { XsdChecker } from "../utils/XsdChecker";
import { PaymentMessage } from '../model/PaymentMessage';

export class WorkerService {

	constructor(
		private xsdChecker: XsdChecker,
		private elemExtractor: ElementExtractor,
		private _containerId: string
	) {}


	get containerId(): string {
		return this.containerId;
	}

	work(msgBody: string): ResultWrapper | undefined {
		let payment: PaymentMessage | undefined = undefined;
		try {
			payment = JSON.parse(msgBody);
		} catch(e: any) {
			console.log('input is not valid json, message will be taken out of the queue regardless')
			return undefined;
		}

		let result: ResultWrapper | undefined = undefined;

		console.log(`batch ${payment!.batchId} - new payment`)

		if (this.xsdChecker.isValidXml(payment!)) {
			console.log(" -> xsd checker: valid xml");

			let extractedElement: string = this.elemExtractor.extract(payment!);

			result = new ResultWrapper(payment!);
			result.appendProcessedTimestamp(new Date())
					.appendContainerId(this._containerId)
					.appendExtractedElem(extractedElement);

		} else {
			console.log(" -> xsd checker: invalid xml");
		}

		return result;
	}


}