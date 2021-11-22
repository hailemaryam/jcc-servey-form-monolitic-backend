import { IQuestionChoice } from 'app/entities/question-choice/question-choice.model';
import { IAnswer } from 'app/entities/answer/answer.model';
import { IForm } from 'app/entities/form/form.model';
import { DataType } from 'app/entities/enumerations/data-type.model';

export interface IQuestion {
  id?: number;
  title?: string | null;
  mandatory?: boolean;
  dataType?: DataType | null;
  questionChoices?: IQuestionChoice[] | null;
  answers?: IAnswer[] | null;
  form?: IForm | null;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public title?: string | null,
    public mandatory?: boolean,
    public dataType?: DataType | null,
    public questionChoices?: IQuestionChoice[] | null,
    public answers?: IAnswer[] | null,
    public form?: IForm | null
  ) {
    this.mandatory = this.mandatory ?? false;
  }
}

export function getQuestionIdentifier(question: IQuestion): number | undefined {
  return question.id;
}
