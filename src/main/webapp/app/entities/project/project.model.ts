import { IFormProgresss } from 'app/entities/form-progresss/form-progresss.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IProject {
  id?: number;
  projectName?: string | null;
  projectDescription?: string | null;
  formProgresses?: IFormProgresss[] | null;
  company?: ICompany | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public projectName?: string | null,
    public projectDescription?: string | null,
    public formProgresses?: IFormProgresss[] | null,
    public company?: ICompany | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
