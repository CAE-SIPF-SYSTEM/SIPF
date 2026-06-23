import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearCompetencia } from './crear-competencia';

describe('CrearCompetencia', () => {
  let component: CrearCompetencia;
  let fixture: ComponentFixture<CrearCompetencia>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearCompetencia],
    }).compileComponents();

    fixture = TestBed.createComponent(CrearCompetencia);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
