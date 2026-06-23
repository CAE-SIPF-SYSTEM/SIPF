import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarCompetencias } from './listar-competencias';

describe('ListarCompetencias', () => {
  let component: ListarCompetencias;
  let fixture: ComponentFixture<ListarCompetencias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarCompetencias],
    }).compileComponents();

    fixture = TestBed.createComponent(ListarCompetencias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
