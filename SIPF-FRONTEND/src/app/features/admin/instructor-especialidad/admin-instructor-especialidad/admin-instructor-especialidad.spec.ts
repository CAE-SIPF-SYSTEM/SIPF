import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminInstructorEspecialidad } from './admin-instructor-especialidad';

describe('AdminInstructorEspecialidad', () => {
  let component: AdminInstructorEspecialidad;
  let fixture: ComponentFixture<AdminInstructorEspecialidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminInstructorEspecialidad],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminInstructorEspecialidad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
