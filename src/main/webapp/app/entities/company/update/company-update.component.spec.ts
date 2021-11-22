jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyService } from '../service/company.service';
import { ICompany, Company } from '../company.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';
import { TypeOfOrganizationService } from 'app/entities/type-of-organization/service/type-of-organization.service';

import { CompanyUpdateComponent } from './company-update.component';

describe('Company Management Update Component', () => {
  let comp: CompanyUpdateComponent;
  let fixture: ComponentFixture<CompanyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let companyService: CompanyService;
  let userService: UserService;
  let typeOfOrganizationService: TypeOfOrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CompanyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CompanyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompanyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    companyService = TestBed.inject(CompanyService);
    userService = TestBed.inject(UserService);
    typeOfOrganizationService = TestBed.inject(TypeOfOrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const company: ICompany = { id: 456 };
      const user: IUser = { id: 67844 };
      company.user = user;

      const userCollection: IUser[] = [{ id: 23320 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ company });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeOfOrganization query and add missing value', () => {
      const company: ICompany = { id: 456 };
      const typeOfOrganation: ITypeOfOrganization = { id: 25664 };
      company.typeOfOrganation = typeOfOrganation;

      const typeOfOrganizationCollection: ITypeOfOrganization[] = [{ id: 19958 }];
      jest.spyOn(typeOfOrganizationService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfOrganizationCollection })));
      const additionalTypeOfOrganizations = [typeOfOrganation];
      const expectedCollection: ITypeOfOrganization[] = [...additionalTypeOfOrganizations, ...typeOfOrganizationCollection];
      jest.spyOn(typeOfOrganizationService, 'addTypeOfOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ company });
      comp.ngOnInit();

      expect(typeOfOrganizationService.query).toHaveBeenCalled();
      expect(typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfOrganizationCollection,
        ...additionalTypeOfOrganizations
      );
      expect(comp.typeOfOrganizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const company: ICompany = { id: 456 };
      const user: IUser = { id: 40071 };
      company.user = user;
      const typeOfOrganation: ITypeOfOrganization = { id: 38376 };
      company.typeOfOrganation = typeOfOrganation;

      activatedRoute.data = of({ company });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(company));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.typeOfOrganizationsSharedCollection).toContain(typeOfOrganation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Company>>();
      const company = { id: 123 };
      jest.spyOn(companyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ company });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: company }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(companyService.update).toHaveBeenCalledWith(company);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Company>>();
      const company = new Company();
      jest.spyOn(companyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ company });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: company }));
      saveSubject.complete();

      // THEN
      expect(companyService.create).toHaveBeenCalledWith(company);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Company>>();
      const company = { id: 123 };
      jest.spyOn(companyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ company });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(companyService.update).toHaveBeenCalledWith(company);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTypeOfOrganizationById', () => {
      it('Should return tracked TypeOfOrganization primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypeOfOrganizationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
