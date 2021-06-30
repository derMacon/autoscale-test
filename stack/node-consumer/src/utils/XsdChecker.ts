import {parseXmlString} from "libxmljs2";
import {PaymentMessage} from '../model/PaymentMessage';
import fs from 'fs';

export class XsdChecker {

    private xsdSpecification: string = '';

    constructor() {
        fs.readFile('./specification.xsd', (error: any, data: any) => {
            if (error) {
                throw error;
            }
            this.xsdSpecification = data.toString();
        });
    }


    isValidXml(payment: PaymentMessage): boolean {
        try {
            var xmlDoc = parseXmlString(payment.content);
        } catch (e) {
            console.log("invalid input xml")
            return false;
        }

        return xmlDoc.validate(parseXmlString(this.xsdSpecification));
    }

}