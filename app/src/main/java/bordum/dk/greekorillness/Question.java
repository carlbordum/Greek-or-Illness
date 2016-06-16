package bordum.dk.greekorillness;

public class Question {

    private String question;
    private boolean isItAGreek;
    private String description;

    public Question(String question){
        toQuestion(question);
    }

    public Question(String question, boolean isItAGreek){

        String[] fullQuestion = question.split(";");
        setQuestion(fullQuestion[0]);
        setDescription(fullQuestion[1]);
        setIsItAGreek(isItAGreek);

    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getQuestion (){
        return this.question;
    }

    public void setIsItAGreek(boolean isItAGreek){
        this.isItAGreek = isItAGreek;
    }

    public Boolean getIsItAGreek (){
        return this.isItAGreek;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public String getQuestionWithDescription(){
        return this.question + this.description;
    }

    @Override
    public String toString(){
        String stringedQuestion = this.question + ";" + Boolean.toString(this.isItAGreek) + ";" + this.description;
        return stringedQuestion;
    }

    public void toQuestion(String question){
        String[] questionValues = question.split(";");
        setQuestion(questionValues[0]);
        setIsItAGreek(Boolean.parseBoolean(questionValues[1]));
        setDescription(questionValues[2]);
    }

}