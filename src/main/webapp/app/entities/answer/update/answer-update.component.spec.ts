jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnswerService } from '../service/answer.service';
import { IAnswer, Answer } from '../answer.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IFormProgresss } from 'app/entities/form-progresss/form-progresss.model';
import { FormProgresssService } from 'app/entities/form-progresss/service/form-progresss.service';

import { AnswerUpdateComponent } from './answer-update.component';

describe('Answer Management Update Component', () => {
  let comp: AnswerUpdateComponent;
  let fixture: ComponentFixture<AnswerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let answerService: AnswerService;
  let userService: UserService;
  let questionService: QuestionService;
  let formProgresssService: FormProgresssService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AnswerUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AnswerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnswerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    answerService = TestBed.inject(AnswerService);
    userService = TestBed.inject(UserService);
    questionService = TestBed.inject(QuestionService);
    formProgresssService = TestBed.inject(FormProgresssService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const answer: IAnswer = { id: 456 };
      const user: IUser = { id: 23286 };
      answer.user = user;

      const userCollection: IUser[] = [{ id: 34372 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Question query and add missing value', () => {
      const answer: IAnswer = { id: 456 };
      const question: IQuestion = { id: 68600 };
      answer.question = question;

      const questionCollection: IQuestion[] = [{ id: 1479 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [question];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(questionCollection, ...additionalQuestions);
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormProgresss query and add missing value', () => {
      const answer: IAnswer = { id: 456 };
      const formProgresss: IFormProgresss = { id: 46940 };
      answer.formProgresss = formProgresss;

      const formProgresssCollection: IFormProgresss[] = [{ id: 68266 }];
      jest.spyOn(formProgresssService, 'query').mockReturnValue(of(new HttpResponse({ body: formProgresssCollection })));
      const additionalFormProgressses = [formProgresss];
      const expectedCollection: IFormProgresss[] = [...additionalFormProgressses, ...formProgresssCollection];
      jest.spyOn(formProgresssService, 'addFormProgresssToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(formProgresssService.query).toHaveBeenCalled();
      expect(formProgresssService.addFormProgresssToCollectionIfMissing).toHaveBeenCalledWith(
        formProgresssCollection,
        ...additionalFormProgressses
      );
      expect(comp.formProgresssesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const answer: IAnswer = { id: 456 };
      const user: IUser = { id: 13196 };
      answer.user = user;
      const question: IQuestion = { id: 62932 };
      answer.question = question;
      const formProgresss: IFormProgresss = { id: 46860 };
      answer.formProgresss = formProgresss;

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(answer));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.questionsSharedCollection).toContain(question);
      expect(comp.formProgresssesSharedCollection).toContain(formProgresss);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = { id: 123 };
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(answerService.update).toHaveBeenCalledWith(answer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = new Answer();
      jest.spyOn(answerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerService.create).toHaveBeenCalledWith(answer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = { id: 123 };
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(answerService.update).toHaveBeenCalledWith(answer);
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

    describe('trackQuestionById', () => {
      it('Should return tracked Question primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormProgresssById', () => {
      it('Should return tracked FormProgresss primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormProgresssById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
