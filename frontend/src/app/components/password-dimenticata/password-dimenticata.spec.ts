import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordDimenticata } from './password-dimenticata';

describe('PasswordDimenticata', () => {
  let component: PasswordDimenticata;
  let fixture: ComponentFixture<PasswordDimenticata>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordDimenticata]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordDimenticata);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
