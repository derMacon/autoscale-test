package dps.hoffmann.springconsumer;

import dps.hoffmann.springconsumer.utils.XmlUtils;

public class Main {

    public static void main(String[] args) {

        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.09\">\n" +
                "  <CstmrCdtTrfInitn>\n" +
                "    <GrpHdr>\n" +
                "      <MsgId>UXC20120800037</MsgId>\n" +
                "      <CreDtTm>2021-05-08T13:30:49.419</CreDtTm>\n" +
                "      <NbOfTxs>1</NbOfTxs>\n" +
                "      <CtrlSum>1717.17</CtrlSum>\n" +
                "      <InitgPty>\n" +
                "        <Nm>Initiating Party Name</Nm>\n" +
                "      </InitgPty>\n" +
                "    </GrpHdr>\n" +
                "    <PmtInf>\n" +
                "      <PmtInfId>UXC20120800037R00001</PmtInfId>\n" +
                "      <PmtMtd>TRF</PmtMtd>\n" +
                "      <NbOfTxs>1</NbOfTxs>\n" +
                "      <CtrlSum>1717.17</CtrlSum>\n" +
                "      <PmtTpInf>\n" +
                "        <SvcLvl>\n" +
                "          <Cd>SDVA</Cd>\n" +
                "        </SvcLvl>\n" +
                "      </PmtTpInf>\n" +
                "      <ReqdExctnDt>\n" +
                "        <Dt>2021-05-08</Dt>  \n" +
                "      </ReqdExctnDt>\n" +
                "      <Dbtr>\n" +
                "        <Nm>GL</Nm>\n" +
                "      </Dbtr>\n" +
                "      <DbtrAcct>\n" +
                "        <Id>\n" +
                "          <IBAN>AT331200000696200104</IBAN>\n" +
                "        </Id>\n" +
                "      </DbtrAcct>\n" +
                "      <DbtrAgt>\n" +
                "        <FinInstnId>\n" +
                "          <BICFI>BKAUATW0XXX</BICFI>\n" +
                "        </FinInstnId>\n" +
                "      </DbtrAgt>\n" +
                "      <CdtTrfTxInf>\n" +
                "        <PmtId>\n" +
                "          <InstrId>UXC20120300037I00001</InstrId>\n" +
                "          <EndToEndId>UXC20120300036E00001</EndToEndId>\n" +
                "        </PmtId>\n" +
                "        <Amt>\n" +
                "          <InstdAmt Ccy=\"EUR\">1717.17</InstdAmt>\n" +
                "        </Amt>\n" +
                "        <CdtrAgt>\n" +
                "          <FinInstnId>\n" +
                "            <BICFI>HYVEDEMM047</BICFI>\n" +
                "          </FinInstnId>\n" +
                "        </CdtrAgt>\n" +
                "        <Cdtr>\n" +
                "          <Nm>DE NAME</Nm>\n" +
                "          <PstlAdr>\n" +
                "            <Ctry>DE</Ctry>\n" +
                "            <AdrLine>Point Courrier 201</AdrLine>\n" +
                "            <AdrLine>IT 91191 Gif sur Yvette</AdrLine>\n" +
                "          </PstlAdr>\n" +
                "        </Cdtr>\n" +
                "        <CdtrAcct>\n" +
                "          <Id>\n" +
                "            <IBAN>DE7979320075037742545600</IBAN>\n" +
                "          </Id>\n" +
                "        </CdtrAcct>\n" +
                "        <RmtInf>\n" +
                "          <Ustrd>Unstructured Remittance Info Val enrich 17</Ustrd>\n" +
                "        </RmtInf>\n" +
                "      </CdtTrfTxInf>\n" +
                "    </PmtInf>\n" +
                "  </CstmrCdtTrfInitn>\n" +
                "</Document>";

        String xPath = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id/IBAN";

        System.out.println(XmlUtils.extractElem(xPath, xmlContent));

    }

}
