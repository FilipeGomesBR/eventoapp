package com.eventoapp.eventoapp.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.eventoapp.model.Convidado;
import com.eventoapp.eventoapp.model.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public ModelAndView form() {
		ModelAndView mv = new ModelAndView("evento/formEvento");
		mv.addObject("eventos",new Evento());
		
		return mv;
	}
	
	@RequestMapping(value="/atualizarEvento/{codigo}",method = RequestMethod.POST)
	public String formEdit(Evento evento) {
		Evento eventoSalvo = er.findByCodigo(evento.getCodigo());
		BeanUtils.copyProperties(evento, eventoSalvo);
		er.save(eventoSalvo);
		return "redirect:/eventos";
	}
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public ModelAndView form(Evento evento) {
		er.save(evento);
		Iterable<Evento> eventoIT = er.findAll();
		ModelAndView mv = new ModelAndView("evento/formEvento");
		mv.addObject("eventos", eventoIT);
		mv.addObject("eventos",new Evento());
		return mv;
	}
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("evento");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos",eventos);
		return mv;
	}
	
	@RequestMapping(value="/{codigo}",method=RequestMethod.GET)
	public ModelAndView detalhesEventos(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento",evento);
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados",convidados);
		mv.addObject("eventos",new Evento());
		return mv;
	}
	
	

	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		Evento evento = er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
		
	}
	
	@RequestMapping(value="/{codigo}",method=RequestMethod.POST)
	public String detalhesEventosPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}"; 
		}
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(long rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		Evento evento = convidado.getEvento();
		String codigo = "" + evento.getCodigo();
		return "redirect:/" + codigo;
		
	}
	
	@RequestMapping(value="/atualizarEvento/{codigo}",method=RequestMethod.GET)
	public ModelAndView atualizar(@PathVariable("codigo") long codigo) {
		ModelAndView mv = new ModelAndView("evento/formEvento");
		Evento eventos = er.findByCodigo(codigo);
		mv.addObject("eventos",eventos);
		return mv;
	}
	



}
