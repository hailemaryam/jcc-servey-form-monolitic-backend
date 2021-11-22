import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormProgresssDetailComponent } from './form-progresss-detail.component';

describe('FormProgresss Management Detail Component', () => {
  let comp: FormProgresssDetailComponent;
  let fixture: ComponentFixture<FormProgresssDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormProgresssDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ formProgresss: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormProgresssDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormProgresssDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load formProgresss on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.formProgresss).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
