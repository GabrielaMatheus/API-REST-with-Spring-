package br.com.alura.forum.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;



@DataJpaTest //anotation pra testar repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Profile("test")
public class CursoRepositoryTest {
//	
//	@Autowired
//	private CursoRepository cursoRepository;
//	
//	//serve pra persistir o banco de dados, pra ele não chegar tão vazio
//	@Autowired
//	private TestEntityManager em;
//
//	@Test
//	void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
//		String nomeCurso = "HTML 5";
//		Curso curso = cursoRepository.findByNome(nomeCurso);
//		Assert.assertNotNull(curso);
//		Assert.assertEquals(nomeCurso,curso.getNome());
		
		
	//}

}
