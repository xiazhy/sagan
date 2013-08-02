package org.springframework.site.indexer.mapper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.site.domain.projects.Project;
import org.springframework.site.domain.projects.Version;
import org.springframework.site.search.SearchEntry;
import org.springframework.site.search.SearchEntryMapper;

import java.util.Date;

public class ApiDocumentMapper implements SearchEntryMapper<Document> {

	private Project project;
	private final Version version;

	public ApiDocumentMapper(Project project, Version version) {
		this.project = project;
		this.version = version;
	}

	public SearchEntry map(Document document) {
		if (document.baseUri().endsWith("allclasses-frame.html")) return null;

		String apiContent;

		Elements blocks = document.select(".block");
		if (blocks.size() > 0) {
			apiContent = blocks.text();
		} else {
			apiContent = document.select("p").text();
		}


		SearchEntry entry = new SearchEntry();
		entry.setPublishAt(new Date(0L));
		entry.setRawContent(apiContent);
		entry.setSummary(apiContent.substring(0, Math.min(apiContent.length(), 500)));
		entry.setTitle(document.title());
		entry.setPath(document.baseUri());
		entry.setCurrent(version.isCurrent());
		entry.addFacetPaths("Documentation", "Documentation/Api", "Projects", "Projects/" + project.getName(),
				"Projects/" + project.getName() + "/" + version.getFullVersion());
		return entry;
	}
}
