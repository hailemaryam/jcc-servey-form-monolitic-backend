import * as dayjs from 'dayjs';

export interface INews {
  id?: number;
  title?: string | null;
  detail?: string | null;
  createdBy?: string | null;
  registeredTime?: dayjs.Dayjs | null;
  updateTime?: dayjs.Dayjs | null;
}

export class News implements INews {
  constructor(
    public id?: number,
    public title?: string | null,
    public detail?: string | null,
    public createdBy?: string | null,
    public registeredTime?: dayjs.Dayjs | null,
    public updateTime?: dayjs.Dayjs | null
  ) {}
}

export function getNewsIdentifier(news: INews): number | undefined {
  return news.id;
}
