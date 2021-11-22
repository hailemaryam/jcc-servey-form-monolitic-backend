import * as dayjs from 'dayjs';
import { IFormProgresss } from 'app/entities/form-progresss/form-progresss.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IUser } from 'app/entities/user/user.model';

export interface IForm {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  formProgressses?: IFormProgresss[] | null;
  questions?: IQuestion[] | null;
  user?: IUser | null;
}

export class Form implements IForm {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null,
    public formProgressses?: IFormProgresss[] | null,
    public questions?: IQuestion[] | null,
    public user?: IUser | null
  ) {}
}

export function getFormIdentifier(form: IForm): number | undefined {
  return form.id;
}
