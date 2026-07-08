import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTrimestres } from './admin-trimestres';

describe('AdminTrimestres', () => {
  let component: AdminTrimestres;
  let fixture: ComponentFixture<AdminTrimestres>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTrimestres],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminTrimestres);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
