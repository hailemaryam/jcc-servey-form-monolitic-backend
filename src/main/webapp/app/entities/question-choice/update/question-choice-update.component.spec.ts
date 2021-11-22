jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionChoiceService } from '../service/question-choice.service';
import { IQuestionChoice, QuestionChoice } from '../question-choice.model';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';

import { QuestionChoiceUpdateComponent } from './question-choice-update.component';

describe('QuestionChoice Management Update Component', () => {
  let comp: QuestionChoiceUpdateComponent;
  let fixture: ComponentFixture<QuestionChoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionChoiceService: QuestionChoiceService;
  let questionService: QuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [QuestionChoiceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(QuestionChoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionChoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionChoiceService = TestBed.inject(QuestionChoiceService);
    questionService = TestBed.inject(QuestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Question query and add missing value', () => {
      const questionChoice: IQuestionChoice = { id: 456 };
      const question: IQuestion = { id: 88171 };
      questionChoice.question = question;

      const questionCollection: IQuestion[] = [{ id: 71729 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [question];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionChoice });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(questionCollection, ...additionalQuestions);
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionChoice: IQuestionChoice = { id: 456 };
      const question: IQuestion = { id: 14496 };
      questionChoice.question = question;

      activatedRoute.data = of({ questionChoice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questionChoice));
      expect(comp.questionsSharedCollection).toContain(question);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionChoice>>();
      const questionChoice = { id: 123 };
      jest.spyOn(questionChoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionChoice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionChoiceService.update).toHaveBeenCalledWith(questionChoice);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionChoice>>();
      const questionChoice = new QuestionChoice();
      jest.spyOn(questionChoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionChoice }));
      saveSubject.complete();

      // THEN
      expect(questionChoiceService.create).toHaveBeenCalledWith(questionChoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionChoice>>();
      const questionChoice = { id: 123 };
      jest.spyOn(questionChoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionChoiceService.update).toHaveBeenCalledWith(questionChoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuestionById', () => {
      it('Should return tracked Question primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
