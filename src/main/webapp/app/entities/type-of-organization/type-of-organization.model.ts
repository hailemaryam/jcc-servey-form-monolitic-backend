import { ICompany } from 'app/entities/company/company.model';

export interface ITypeOfOrganization {
  id?: number;
  name?: string | null;
  description?: string | null;
  companies?: ICompany[] | null;
}

export class TypeOfOrganization implements ITypeOfOrganization {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public companies?: ICompany[] | null) {}
}

export function getTypeOfOrganizationIdentifier(typeOfOrganization: ITypeOfOrganization): number | undefined {
  return typeOfOrganization.id;
}
