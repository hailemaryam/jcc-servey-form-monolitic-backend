jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';
import { IMultipleChoiceAnsewer, MultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';
import { IAnswer } from 'app/entities/answer/answer.model';
import { AnswerService } from 'app/entities/answer/service/answer.service';

import { MultipleChoiceAnsewerUpdateComponent } from './multiple-choice-ansewer-update.component';

describe('MultipleChoiceAnsewer Management Update Component', () => {
  let comp: MultipleChoiceAnsewerUpdateComponent;
  let fixture: ComponentFixture<MultipleChoiceAnsewerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let multipleChoiceAnsewerService: MultipleChoiceAnsewerService;
  let answerService: AnswerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MultipleChoiceAnsewerUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MultipleChoiceAnsewerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MultipleChoiceAnsewerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    multipleChoiceAnsewerService = TestBed.inject(MultipleChoiceAnsewerService);
    answerService = TestBed.inject(AnswerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Answer query and add missing value', () => {
      const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 456 };
      const answer: IAnswer = { id: 79638 };
      multipleChoiceAnsewer.answer = answer;

      const answerCollection: IAnswer[] = [{ id: 57158 }];
      jest.spyOn(answerService, 'query').mockReturnValue(of(new HttpResponse({ body: answerCollection })));
      const additionalAnswers = [answer];
      const expectedCollection: IAnswer[] = [...additionalAnswers, ...answerCollection];
      jest.spyOn(answerService, 'addAnswerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ multipleChoiceAnsewer });
      comp.ngOnInit();

      expect(answerService.query).toHaveBeenCalled();
      expect(answerService.addAnswerToCollectionIfMissing).toHaveBeenCalledWith(answerCollection, ...additionalAnswers);
      expect(comp.answersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 456 };
      const answer: IAnswer = { id: 56626 };
      multipleChoiceAnsewer.answer = answer;

      activatedRoute.data = of({ multipleChoiceAnsewer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(multipleChoiceAnsewer));
      expect(comp.answersSharedCollection).toContain(answer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MultipleChoiceAnsewer>>();
      const multipleChoiceAnsewer = { id: 123 };
      jest.spyOn(multipleChoiceAnsewerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ multipleChoiceAnsewer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: multipleChoiceAnsewer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(multipleChoiceAnsewerService.update).toHaveBeenCalledWith(multipleChoiceAnsewer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MultipleChoiceAnsewer>>();
      const multipleChoiceAnsewer = new MultipleChoiceAnsewer();
      jest.spyOn(multipleChoiceAnsewerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ multipleChoiceAnsewer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: multipleChoiceAnsewer }));
      saveSubject.complete();

      // THEN
      expect(multipleChoiceAnsewerService.create).toHaveBeenCalledWith(multipleChoiceAnsewer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MultipleChoiceAnsewer>>();
      const multipleChoiceAnsewer = { id: 123 };
      jest.spyOn(multipleChoiceAnsewerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ multipleChoiceAnsewer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(multipleChoiceAnsewerService.update).toHaveBeenCalledWith(multipleChoiceAnsewer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAnswerById', () => {
      it('Should return tracked Answer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAnswerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
