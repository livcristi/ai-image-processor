package org.ubb.image_handler_service.dto.interaction;

import org.ubb.image_handler_service.dto.ObjectInfoResponse;

import java.util.Arrays;
import java.util.Objects;

public class InteractionContent
{
    private InteractionResponse interactionMetadata;
    private ObjectInfoResponse inputMetadata;
    private ObjectInfoResponse resultMetadata;

    byte[] inputImage;
    byte[] resultData;

    public InteractionContent()
    {
        // Empty constructor
    }

    public InteractionResponse getInteractionMetadata()
    {
        return interactionMetadata;
    }

    public void setInteractionMetadata(InteractionResponse interactionMetadata)
    {
        this.interactionMetadata = interactionMetadata;
    }

    public ObjectInfoResponse getInputMetadata()
    {
        return inputMetadata;
    }

    public void setInputMetadata(ObjectInfoResponse inputMetadata)
    {
        this.inputMetadata = inputMetadata;
    }

    public ObjectInfoResponse getResultMetadata()
    {
        return resultMetadata;
    }

    public void setResultMetadata(ObjectInfoResponse resultMetadata)
    {
        this.resultMetadata = resultMetadata;
    }

    public byte[] getInputImage()
    {
        return inputImage;
    }

    public void setInputImage(byte[] inputImage)
    {
        this.inputImage = inputImage;
    }

    public byte[] getResultData()
    {
        return resultData;
    }

    public void setResultData(byte[] resultData)
    {
        this.resultData = resultData;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionContent that = (InteractionContent) o;
        return Objects.equals(interactionMetadata, that.interactionMetadata) && Objects.equals(inputMetadata, that.inputMetadata) && Objects.equals(resultMetadata, that.resultMetadata) && Objects.deepEquals(inputImage, that.inputImage) && Objects.deepEquals(resultData, that.resultData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interactionMetadata, inputMetadata, resultMetadata, Arrays.hashCode(inputImage), Arrays.hashCode(resultData));
    }

    @Override
    public String toString()
    {
        return "InteractionContent{" +
                "interactionMetadata=" + interactionMetadata +
                ", inputMetadata=" + inputMetadata +
                ", outputMetadata=" + resultMetadata +
                ", inputImage=" + Arrays.toString(inputImage) +
                ", outputData=" + Arrays.toString(resultData) +
                '}';
    }
}
