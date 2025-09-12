// @ts-ignore
import {Component, Input, OnInit} from '@angular/core';
import {CrudViewComponent} from '../crud-view.component';

// @ts-ignore
@Component({
  selector: 'form-page',
  templateUrl: 'form-page.component.html',
  standalone: false
})
export class FormPageComponent extends CrudViewComponent implements OnInit {

  // Armazena o link para voltar para a tela de consulta
  @Input() backLink: string | undefined;

  ngOnInit() {
    this.handleSubhead();
  }

  // Manipula o subtítulo da página de FORM
  handleSubhead = () => this.subhead = this.entity && this.entity.id ? `${this.subhead} / Editar` : `${this.subhead} / Adicionar`;
}
