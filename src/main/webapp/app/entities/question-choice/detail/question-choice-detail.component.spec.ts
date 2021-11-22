import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionChoiceDetailComponent } from './question-choice-detail.component';

describe('QuestionChoice Management Detail Component', () => {
  let comp: QuestionChoiceDetailComponent;
  let fixture: ComponentFixture<QuestionChoiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionChoiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questionChoice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestionChoiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestionChoiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questionChoice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questionChoice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
