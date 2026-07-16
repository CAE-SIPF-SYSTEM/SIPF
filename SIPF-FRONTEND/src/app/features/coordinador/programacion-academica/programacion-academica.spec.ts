import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramacionAcademica } from './programacion-academica';

describe('ProgramacionAcademica', () => {
  let component: ProgramacionAcademica;
  let fixture: ComponentFixture<ProgramacionAcademica>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramacionAcademica],
    }).compileComponents();

    fixture = TestBed.createComponent(ProgramacionAcademica);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
