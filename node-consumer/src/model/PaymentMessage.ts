import { PaymentInput } from './PaymentInput';

// src: https://stackoverflow.com/questions/34031448/typescript-typeerror-myclass-myfunction-is-not-a-function
export class PaymentMessage {

	constructor(private initData: PaymentInput) {}

	get batchId(): number {
		 return this.initData.batchId;
	};

	get content(): string {
		 return this.initData.content;
	};

	get xpath(): string {
		return this.initData.xpath;
	}

	get sentTimestamp(): Date {
		return this.initData.sentTimestamp
	}

}