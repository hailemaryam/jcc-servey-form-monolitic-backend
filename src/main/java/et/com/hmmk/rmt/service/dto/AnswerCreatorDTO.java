package et.com.hmmk.rmt.service.dto;

import et.com.hmmk.rmt.domain.enumeration.DataType;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

public class AnswerCreatorDTO implements Serializable {

    private Long id;

    private Double number;

    private Boolean booleanAnswer;

    private String shortAnswer;

    @Lob
    private String paragraph;

    private String multipleChoice;

    private String dropDown;

    @Lob
    private byte[] fileUploaded;

    private String fileUploadedContentType;
    private String fileName;

    private Instant date;

    private Duration time;

    private Instant submitedOn;

    private DataType dataType;

    private UserDTO user;

    private QuestionDTO question;

    private FormProgresssDTO formProgresss;

    private Set<MultipleChoiceAnsewerDTO> multipleChoiceAnsewerDTOS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Boolean getBooleanAnswer() {
        return booleanAnswer;
    }

    public void setBooleanAnswer(Boolean booleanAnswer) {
        this.booleanAnswer = booleanAnswer;
    }

    public String getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(String shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(String multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public String getDropDown() {
        return dropDown;
    }

    public void setDropDown(String dropDown) {
        this.dropDown = dropDown;
    }

    public byte[] getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(byte[] fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getFileUploadedContentType() {
        return fileUploadedContentType;
    }

    public void setFileUploadedContentType(String fileUploadedContentType) {
        this.fileUploadedContentType = fileUploadedContentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Instant getSubmitedOn() {
        return submitedOn;
    }

    public void setSubmitedOn(Instant submitedOn) {
        this.submitedOn = submitedOn;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public FormProgresssDTO getFormProgresss() {
        return formProgresss;
    }

    public void setFormProgresss(FormProgresssDTO formProgresss) {
        this.formProgresss = formProgresss;
    }

    public Set<MultipleChoiceAnsewerDTO> getMultipleChoiceAnsewerDTOS() {
        return multipleChoiceAnsewerDTOS;
    }

    public void setMultipleChoiceAnsewerDTOS(Set<MultipleChoiceAnsewerDTO> multipleChoiceAnsewerDTOS) {
        this.multipleChoiceAnsewerDTOS = multipleChoiceAnsewerDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerCreatorDTO)) {
            return false;
        }

        AnswerCreatorDTO answerDTO = (AnswerCreatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", booleanAnswer='" + getBooleanAnswer() + "'" +
            ", shortAnswer='" + getShortAnswer() + "'" +
            ", paragraph='" + getParagraph() + "'" +
            ", multipleChoice='" + getMultipleChoice() + "'" +
            ", dropDown='" + getDropDown() + "'" +
            ", fileUploaded='" + getFileUploaded() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", date='" + getDate() + "'" +
            ", time='" + getTime() + "'" +
            ", submitedOn='" + getSubmitedOn() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", user=" + getUser() +
            ", question=" + getQuestion() +
            ", formProgresss=" + getFormProgresss() +
            "}";
    }
}
