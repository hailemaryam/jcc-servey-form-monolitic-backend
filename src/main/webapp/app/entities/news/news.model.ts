import * as dayjs from 'dayjs';

export interface INews {
  id?: number;
  featuredImageContentType?: string | null;
  featuredImage?: string | null;
  featuredImageUrl?: string | null;
  title?: string | null;
  detail?: string | null;
  createdBy?: string | null;
  registeredTime?: dayjs.Dayjs | null;
  updateTime?: dayjs.Dayjs | null;
}

export class News implements INews {
  constructor(
    public id?: number,
    public featuredImageContentType?: string | null,
    public featuredImage?: string | null,
    public featuredImageUrl?: string | null,
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
