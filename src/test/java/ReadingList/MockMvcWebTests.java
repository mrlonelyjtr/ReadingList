package ReadingList;

import ReadingList.model.Book;
import ReadingList.model.Reader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcWebTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void homePage_unauthenticatedUser() throws Exception {
		mockMvc.perform(get("/readingList"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "http://localhost/login"));
	}

	@Test
	@WithUserDetails("mrlonelyjtr")
	public void homePage_authenticatedUser() throws Exception {
		Reader expectedReader = new Reader();
		expectedReader.setUsername("mrlonelyjtr");
		expectedReader.setFullname("MrLonelyJTR");
		expectedReader.setPassword("1234");

		Book expectedBook = new Book();
		expectedBook.setId(1L);
		expectedBook.setTitle("And Then There Were None");
		expectedBook.setAuthor("Agatha Christie");
		expectedBook.setIsbn("0007136838");
		expectedBook.setDescription("无人生还");

		mockMvc.perform(get("/readingList"))
				.andExpect(status().isOk())
				.andExpect(view().name("readingList"))
				.andExpect(model().attribute("books", empty()))
				.andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
				.andExpect(model().attribute("amazonID", "mrlonelyjtr"));

		mockMvc.perform(post("/readingList").with(csrf())
				.contentType(APPLICATION_FORM_URLENCODED)
				.param("title", "And Then There Were None")
				.param("author", "Agatha Christie")
				.param("isbn", "0007136838")
				.param("description", "无人生还"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/readingList"));

		mockMvc.perform(get("/readingList"))
				.andExpect(status().isOk())
				.andExpect(view().name("readingList"))
				.andExpect(model().attributeExists("books"))
				.andExpect(model().attribute("books", hasSize(1)));
	}

}
