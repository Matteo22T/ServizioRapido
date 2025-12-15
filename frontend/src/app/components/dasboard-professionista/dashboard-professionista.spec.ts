import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DasboardProfessionista } from './dashboard-professionista';

describe('DasboardProfessionista', () => {
  let component: DasboardProfessionista;
  let fixture: ComponentFixture<DasboardProfessionista>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DasboardProfessionista]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DasboardProfessionista);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
