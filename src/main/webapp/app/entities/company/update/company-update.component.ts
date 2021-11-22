import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompany, Company } from '../company.model';
import { CompanyService } from '../service/company.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';
import { TypeOfOrganizationService } from 'app/entities/type-of-organization/service/type-of-organization.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  typeOfOrganizationsSharedCollection: ITypeOfOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    strategicObjective: [],
    futureFocusArea: [],
    currentFundingCycle: [],
    user: [],
    typeOfOrganation: [],
  });

  constructor(
    protected companyService: CompanyService,
    protected userService: UserService,
    protected typeOfOrganizationService: TypeOfOrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTypeOfOrganizationById(index: number, item: ITypeOfOrganization): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      strategicObjective: company.strategicObjective,
      futureFocusArea: company.futureFocusArea,
      currentFundingCycle: company.currentFundingCycle,
      user: company.user,
      typeOfOrganation: company.typeOfOrganation,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, company.user);
    this.typeOfOrganizationsSharedCollection = this.typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing(
      this.typeOfOrganizationsSharedCollection,
      company.typeOfOrganation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.typeOfOrganizationService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfOrganization[]>) => res.body ?? []))
      .pipe(
        map((typeOfOrganizations: ITypeOfOrganization[]) =>
          this.typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing(
            typeOfOrganizations,
            this.editForm.get('typeOfOrganation')!.value
          )
        )
      )
      .subscribe((typeOfOrganizations: ITypeOfOrganization[]) => (this.typeOfOrganizationsSharedCollection = typeOfOrganizations));
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      strategicObjective: this.editForm.get(['strategicObjective'])!.value,
      futureFocusArea: this.editForm.get(['futureFocusArea'])!.value,
      currentFundingCycle: this.editForm.get(['currentFundingCycle'])!.value,
      user: this.editForm.get(['user'])!.value,
      typeOfOrganation: this.editForm.get(['typeOfOrganation'])!.value,
    };
  }
}
