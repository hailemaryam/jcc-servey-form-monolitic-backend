package et.com.hmmk.rmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import et.com.hmmk.rmt.domain.enumeration.DataType;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Double number;

    @Column(name = "boolean_answer")
    private Boolean booleanAnswer;

    @Column(name = "short_answer")
    private String shortAnswer;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "paragraph")
    private String paragraph;

    @Column(name = "multiple_choice")
    private String multipleChoice;

    @Column(name = "drop_down")
    private String dropDown;

    @Lob
    @Column(name = "file_uploaded")
    private byte[] fileUploaded;

    @Column(name = "file_uploaded_content_type")
    private String fileUploadedContentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "date")
    private Instant date;

    @Column(name = "time")
    private Duration time;

    @Column(name = "submited_on")
    private Instant submitedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @OneToMany(mappedBy = "answer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answer" }, allowSetters = true)
    private Set<MultipleChoiceAnsewer> multipleChoiceAnsewers = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questionChoices", "answers", "form" }, allowSetters = true)
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties(value = { "answers", "user", "form" }, allowSetters = true)
    private FormProgresss formProgresss;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumber() {
        return this.number;
    }

    public Answer number(Double number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Boolean getBooleanAnswer() {
        return this.booleanAnswer;
    }

    public Answer booleanAnswer(Boolean booleanAnswer) {
        this.setBooleanAnswer(booleanAnswer);
        return this;
    }

    public void setBooleanAnswer(Boolean booleanAnswer) {
        this.booleanAnswer = booleanAnswer;
    }

    public String getShortAnswer() {
        return this.shortAnswer;
    }

    public Answer shortAnswer(String shortAnswer) {
        this.setShortAnswer(shortAnswer);
        return this;
    }

    public void setShortAnswer(String shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public String getParagraph() {
        return this.paragraph;
    }

    public Answer paragraph(String paragraph) {
        this.setParagraph(paragraph);
        return this;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getMultipleChoice() {
        return this.multipleChoice;
    }

    public Answer multipleChoice(String multipleChoice) {
        this.setMultipleChoice(multipleChoice);
        return this;
    }

    public void setMultipleChoice(String multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public String getDropDown() {
        return this.dropDown;
    }

    public Answer dropDown(String dropDown) {
        this.setDropDown(dropDown);
        return this;
    }

    public void setDropDown(String dropDown) {
        this.dropDown = dropDown;
    }

    public byte[] getFileUploaded() {
        return this.fileUploaded;
    }

    public Answer fileUploaded(byte[] fileUploaded) {
        this.setFileUploaded(fileUploaded);
        return this;
    }

    public void setFileUploaded(byte[] fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getFileUploadedContentType() {
        return this.fileUploadedContentType;
    }

    public Answer fileUploadedContentType(String fileUploadedContentType) {
        this.fileUploadedContentType = fileUploadedContentType;
        return this;
    }

    public void setFileUploadedContentType(String fileUploadedContentType) {
        this.fileUploadedContentType = fileUploadedContentType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Answer fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getDate() {
        return this.date;
    }

    public Answer date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Duration getTime() {
        return this.time;
    }

    public Answer time(Duration time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Instant getSubmitedOn() {
        return this.submitedOn;
    }

    public Answer submitedOn(Instant submitedOn) {
        this.setSubmitedOn(submitedOn);
        return this;
    }

    public void setSubmitedOn(Instant submitedOn) {
        this.submitedOn = submitedOn;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public Answer dataType(DataType dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Set<MultipleChoiceAnsewer> getMultipleChoiceAnsewers() {
        return this.multipleChoiceAnsewers;
    }

    public void setMultipleChoiceAnsewers(Set<MultipleChoiceAnsewer> multipleChoiceAnsewers) {
        if (this.multipleChoiceAnsewers != null) {
            this.multipleChoiceAnsewers.forEach(i -> i.setAnswer(null));
        }
        if (multipleChoiceAnsewers != null) {
            multipleChoiceAnsewers.forEach(i -> i.setAnswer(this));
        }
        this.multipleChoiceAnsewers = multipleChoiceAnsewers;
    }

    public Answer multipleChoiceAnsewers(Set<MultipleChoiceAnsewer> multipleChoiceAnsewers) {
        this.setMultipleChoiceAnsewers(multipleChoiceAnsewers);
        return this;
    }

    public Answer addMultipleChoiceAnsewer(MultipleChoiceAnsewer multipleChoiceAnsewer) {
        this.multipleChoiceAnsewers.add(multipleChoiceAnsewer);
        multipleChoiceAnsewer.setAnswer(this);
        return this;
    }

    public Answer removeMultipleChoiceAnsewer(MultipleChoiceAnsewer multipleChoiceAnsewer) {
        this.multipleChoiceAnsewers.remove(multipleChoiceAnsewer);
        multipleChoiceAnsewer.setAnswer(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer user(User user) {
        this.setUser(user);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public FormProgresss getFormProgresss() {
        return this.formProgresss;
    }

    public void setFormProgresss(FormProgresss formProgresss) {
        this.formProgresss = formProgresss;
    }

    public Answer formProgresss(FormProgresss formProgresss) {
        this.setFormProgresss(formProgresss);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", booleanAnswer='" + getBooleanAnswer() + "'" +
            ", shortAnswer='" + getShortAnswer() + "'" +
            ", paragraph='" + getParagraph() + "'" +
            ", multipleChoice='" + getMultipleChoice() + "'" +
            ", dropDown='" + getDropDown() + "'" +
            ", fileUploaded='" + getFileUploaded() + "'" +
            ", fileUploadedContentType='" + getFileUploadedContentType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", date='" + getDate() + "'" +
            ", time='" + getTime() + "'" +
            ", submitedOn='" + getSubmitedOn() + "'" +
            ", dataType='" + getDataType() + "'" +
            "}";
    }
}
