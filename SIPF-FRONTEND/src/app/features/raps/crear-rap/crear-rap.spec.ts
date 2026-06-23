import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearRap } from './crear-rap';

describe('CrearRap', () => {
  let component: CrearRap;
  let fixture: ComponentFixture<CrearRap>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearRap],
    }).compileComponents();

    fixture = TestBed.createComponent(CrearRap);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
