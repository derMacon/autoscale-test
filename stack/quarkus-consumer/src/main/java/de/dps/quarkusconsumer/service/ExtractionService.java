package de.dps.quarkusconsumer.service;

import de.dps.quarkusconsumer.model.ContainerInfo;
import de.dps.quarkusconsumer.model.InputPaymentMsg;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import de.dps.quarkusconsumer.utils.XmlUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import static de.dps.quarkusconsumer.utils.XmlUtils.extractElem;

@ApplicationScoped
public class ExtractionService {

    @ConfigProperty(name = "outputPaymentMsg.schema.xsdPath")
    String xsdPath;

    private static final Logger LOG = Logger.getLogger(ExtractionService.class);

    private ContainerInfo containerInfo;

    public ExtractionService(ContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public OutputPaymentMsg createPayment(InputPaymentMsg inputMessage) {
        if (inputMessage == null
                || !XmlUtils.validateAgainstXSD(inputMessage.getContent(), xsdPath)) {
            return null;
        }

        String extractedElem = extractElem(inputMessage.getXpath(), inputMessage.getContent());

        OutputPaymentMsg out =  new OutputPaymentMsg(inputMessage);
        out.setContainerId(containerInfo.getId());
        out.setExtractedElement(extractedElem);

        return out;
    }

}
