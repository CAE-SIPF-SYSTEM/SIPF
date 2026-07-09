import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignarEspecialidadDialog } from './asignar-especialidad-dialog';

describe('AsignarEspecialidadDialog', () => {
  let component: AsignarEspecialidadDialog;
  let fixture: ComponentFixture<AsignarEspecialidadDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignarEspecialidadDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(AsignarEspecialidadDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
