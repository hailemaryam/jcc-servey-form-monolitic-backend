import { IQuestion } from 'app/entities/question/question.model';

export interface IQuestionChoice {
  id?: number;
  option?: string | null;
  question?: IQuestion | null;
}

export class QuestionChoice implements IQuestionChoice {
  constructor(public id?: number, public option?: string | null, public question?: IQuestion | null) {}
}

export function getQuestionChoiceIdentifier(questionChoice: IQuestionChoice): number | undefined {
  return questionChoice.id;
}
