jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormProgresssService } from '../service/form-progresss.service';
import { IFormProgresss, FormProgresss } from '../form-progresss.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

import { FormProgresssUpdateComponent } from './form-progresss-update.component';

describe('FormProgresss Management Update Component', () => {
  let comp: FormProgresssUpdateComponent;
  let fixture: ComponentFixture<FormProgresssUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formProgresssService: FormProgresssService;
  let userService: UserService;
  let formService: FormService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormProgresssUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FormProgresssUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormProgresssUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formProgresssService = TestBed.inject(FormProgresssService);
    userService = TestBed.inject(UserService);
    formService = TestBed.inject(FormService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const formProgresss: IFormProgresss = { id: 456 };
      const user: IUser = { id: 54396 };
      formProgresss.user = user;

      const userCollection: IUser[] = [{ id: 61322 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Form query and add missing value', () => {
      const formProgresss: IFormProgresss = { id: 456 };
      const form: IForm = { id: 52001 };
      formProgresss.form = form;

      const formCollection: IForm[] = [{ id: 93983 }];
      jest.spyOn(formService, 'query').mockReturnValue(of(new HttpResponse({ body: formCollection })));
      const additionalForms = [form];
      const expectedCollection: IForm[] = [...additionalForms, ...formCollection];
      jest.spyOn(formService, 'addFormToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      expect(formService.query).toHaveBeenCalled();
      expect(formService.addFormToCollectionIfMissing).toHaveBeenCalledWith(formCollection, ...additionalForms);
      expect(comp.formsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const formProgresss: IFormProgresss = { id: 456 };
      const project: IProject = { id: 75776 };
      formProgresss.project = project;

      const projectCollection: IProject[] = [{ id: 92003 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(projectCollection, ...additionalProjects);
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formProgresss: IFormProgresss = { id: 456 };
      const user: IUser = { id: 57334 };
      formProgresss.user = user;
      const form: IForm = { id: 68357 };
      formProgresss.form = form;
      const project: IProject = { id: 54902 };
      formProgresss.project = project;

      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(formProgresss));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.formsSharedCollection).toContain(form);
      expect(comp.projectsSharedCollection).toContain(project);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormProgresss>>();
      const formProgresss = { id: 123 };
      jest.spyOn(formProgresssService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formProgresss }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(formProgresssService.update).toHaveBeenCalledWith(formProgresss);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormProgresss>>();
      const formProgresss = new FormProgresss();
      jest.spyOn(formProgresssService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formProgresss }));
      saveSubject.complete();

      // THEN
      expect(formProgresssService.create).toHaveBeenCalledWith(formProgresss);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormProgresss>>();
      const formProgresss = { id: 123 };
      jest.spyOn(formProgresssService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formProgresss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formProgresssService.update).toHaveBeenCalledWith(formProgresss);
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

    describe('trackFormById', () => {
      it('Should return tracked Form primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProjectById', () => {
      it('Should return tracked Project primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProjectById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
