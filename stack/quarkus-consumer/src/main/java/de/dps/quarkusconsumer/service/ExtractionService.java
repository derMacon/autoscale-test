package de.dps.quarkusconsumer.service;

import de.dps.quarkusconsumer.model.ContainerInfo;
import de.dps.quarkusconsumer.model.InputPaymentMsg;
import de.dps.quarkusconsumer.model.OutputPaymentMsg;
import de.dps.quarkusconsumer.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

import static de.dps.quarkusconsumer.utils.XmlUtils.extractElem;

@ApplicationScoped
@Slf4j
public class ExtractionService {

    @ConfigProperty(name = "outputPaymentMsg.schema.xsdPath")
    String xsdPath;

    private ContainerInfo containerInfo;

    public ExtractionService(ContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public OutputPaymentMsg createPayment(InputPaymentMsg inputMessage) {
        if (!XmlUtils.validateAgainstXSD(inputMessage.getContent(), xsdPath)) {
            return null;
        }

        String extractedElem = extractElem(inputMessage.getXpath(), inputMessage.getContent());

        return new OutputPaymentMsg(inputMessage)
                .withContainerId(containerInfo.getId())
                .withExtractedElement(extractedElem);
    }

}
