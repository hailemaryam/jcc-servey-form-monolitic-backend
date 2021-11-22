import * as dayjs from 'dayjs';
import { IMultipleChoiceAnsewer } from 'app/entities/multiple-choice-ansewer/multiple-choice-ansewer.model';
import { IUser } from 'app/entities/user/user.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IFormProgresss } from 'app/entities/form-progresss/form-progresss.model';
import { DataType } from 'app/entities/enumerations/data-type.model';

export interface IAnswer {
  id?: number;
  number?: number | null;
  booleanAnswer?: boolean | null;
  shortAnswer?: string | null;
  paragraph?: string | null;
  multipleChoice?: string | null;
  dropDown?: string | null;
  fileUploadedContentType?: string | null;
  fileUploaded?: string | null;
  fileName?: string | null;
  date?: dayjs.Dayjs | null;
  time?: string | null;
  submitedOn?: dayjs.Dayjs | null;
  dataType?: DataType | null;
  multipleChoiceAnsewers?: IMultipleChoiceAnsewer[] | null;
  user?: IUser | null;
  question?: IQuestion | null;
  formProgresss?: IFormProgresss | null;
}

export class Answer implements IAnswer {
  constructor(
    public id?: number,
    public number?: number | null,
    public booleanAnswer?: boolean | null,
    public shortAnswer?: string | null,
    public paragraph?: string | null,
    public multipleChoice?: string | null,
    public dropDown?: string | null,
    public fileUploadedContentType?: string | null,
    public fileUploaded?: string | null,
    public fileName?: string | null,
    public date?: dayjs.Dayjs | null,
    public time?: string | null,
    public submitedOn?: dayjs.Dayjs | null,
    public dataType?: DataType | null,
    public multipleChoiceAnsewers?: IMultipleChoiceAnsewer[] | null,
    public user?: IUser | null,
    public question?: IQuestion | null,
    public formProgresss?: IFormProgresss | null
  ) {
    this.booleanAnswer = this.booleanAnswer ?? false;
  }
}

export function getAnswerIdentifier(answer: IAnswer): number | undefined {
  return answer.id;
}
