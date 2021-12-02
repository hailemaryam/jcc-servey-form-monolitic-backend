import { IUser } from 'app/entities/user/user.model';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';

export interface ICompany {
  id?: number;
  companyName?: string | null;
  strategicObjective?: string | null;
  futureFocusArea?: string | null;
  currentFundingCycle?: string | null;
  user?: IUser | null;
  typeOfOrganation?: ITypeOfOrganization | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public companyName?: string | null,
    public strategicObjective?: string | null,
    public futureFocusArea?: string | null,
    public currentFundingCycle?: string | null,
    public user?: IUser | null,
    public typeOfOrganation?: ITypeOfOrganization | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
