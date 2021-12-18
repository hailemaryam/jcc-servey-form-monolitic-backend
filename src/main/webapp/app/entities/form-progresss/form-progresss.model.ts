import * as dayjs from 'dayjs';
import { IAnswer } from 'app/entities/answer/answer.model';
import { IUser } from 'app/entities/user/user.model';
import { IForm } from 'app/entities/form/form.model';
import { IProject } from 'app/entities/project/project.model';

export interface IFormProgresss {
  id?: number;
  submited?: boolean | null;
  startedOn?: dayjs.Dayjs | null;
  submitedOn?: dayjs.Dayjs | null;
  sentedOn?: dayjs.Dayjs | null;
  answers?: IAnswer[] | null;
  user?: IUser | null;
  form?: IForm | null;
  project?: IProject | null;
}

export class FormProgresss implements IFormProgresss {
  constructor(
    public id?: number,
    public submited?: boolean | null,
    public startedOn?: dayjs.Dayjs | null,
    public submitedOn?: dayjs.Dayjs | null,
    public sentedOn?: dayjs.Dayjs | null,
    public answers?: IAnswer[] | null,
    public user?: IUser | null,
    public form?: IForm | null,
    public project?: IProject | null
  ) {
    this.submited = this.submited ?? false;
  }
}

export function getFormProgresssIdentifier(formProgresss: IFormProgresss): number | undefined {
  return formProgresss.id;
}
