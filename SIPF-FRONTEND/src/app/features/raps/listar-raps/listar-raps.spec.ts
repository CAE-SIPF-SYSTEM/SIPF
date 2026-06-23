import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarRaps } from './listar-raps';

describe('ListarRaps', () => {
  let component: ListarRaps;
  let fixture: ComponentFixture<ListarRaps>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarRaps],
    }).compileComponents();

    fixture = TestBed.createComponent(ListarRaps);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
