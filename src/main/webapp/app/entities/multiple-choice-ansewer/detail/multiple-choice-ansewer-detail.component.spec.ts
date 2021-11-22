import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MultipleChoiceAnsewerDetailComponent } from './multiple-choice-ansewer-detail.component';

describe('MultipleChoiceAnsewer Management Detail Component', () => {
  let comp: MultipleChoiceAnsewerDetailComponent;
  let fixture: ComponentFixture<MultipleChoiceAnsewerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MultipleChoiceAnsewerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ multipleChoiceAnsewer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MultipleChoiceAnsewerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MultipleChoiceAnsewerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load multipleChoiceAnsewer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.multipleChoiceAnsewer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
