package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralExtractor implements CameraDomainExtractor {

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {
        DocumentEntropyTree d = new DocumentEntropyTree(document);
        return null;
    }

    private static class DocumentEntropyTree {
        ElementEntropyNode root;

        private DocumentEntropyTree(Document document) {
            root = new ElementEntropyNode(document);
        }

        private static class ElementEntropyNode {
            Element element;
            List<ElementEntropyNode> children;
            int allChildrenCount;
            double entropy;

            public ElementEntropyNode(Element element) {
                this.element = element;
                children = element.children().stream()
                        .map(ElementEntropyNode::new)
                        .collect(Collectors.toList());
                    children.stream().mapToDouble(node -> node.entropy);
                allChildrenCount = children.stream().mapToInt(children -> children.allChildrenCount).sum() + 1;
                if (allChildrenCount == 1) {
                    entropy = 0;
                } else {
                    entropy = -children.stream().mapToDouble(children -> children.allChildrenCount * Math.log(children.allChildrenCount)).sum();
                }
            }
        }

    }
}
