import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardProfessionista } from './dashboard-professionista';

describe('DasboardProfessionista', () => {
  let component: DashboardProfessionista;
  let fixture: ComponentFixture<DashboardProfessionista>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardProfessionista]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardProfessionista);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
