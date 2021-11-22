jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeOfOrganizationService } from '../service/type-of-organization.service';
import { ITypeOfOrganization, TypeOfOrganization } from '../type-of-organization.model';

import { TypeOfOrganizationUpdateComponent } from './type-of-organization-update.component';

describe('TypeOfOrganization Management Update Component', () => {
  let comp: TypeOfOrganizationUpdateComponent;
  let fixture: ComponentFixture<TypeOfOrganizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeOfOrganizationService: TypeOfOrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeOfOrganizationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TypeOfOrganizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeOfOrganizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeOfOrganizationService = TestBed.inject(TypeOfOrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeOfOrganization: ITypeOfOrganization = { id: 456 };

      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typeOfOrganization));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeOfOrganization>>();
      const typeOfOrganization = { id: 123 };
      jest.spyOn(typeOfOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfOrganization }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeOfOrganizationService.update).toHaveBeenCalledWith(typeOfOrganization);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeOfOrganization>>();
      const typeOfOrganization = new TypeOfOrganization();
      jest.spyOn(typeOfOrganizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfOrganization }));
      saveSubject.complete();

      // THEN
      expect(typeOfOrganizationService.create).toHaveBeenCalledWith(typeOfOrganization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeOfOrganization>>();
      const typeOfOrganization = { id: 123 };
      jest.spyOn(typeOfOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeOfOrganizationService.update).toHaveBeenCalledWith(typeOfOrganization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
