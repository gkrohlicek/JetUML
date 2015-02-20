package ca.mcgill.cs.stg.jetuml.framework;

import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ca.mcgill.cs.stg.jetuml.graph.CallEdge;
import ca.mcgill.cs.stg.jetuml.graph.CallNode;
import ca.mcgill.cs.stg.jetuml.graph.CircularStateNode;
import ca.mcgill.cs.stg.jetuml.graph.ClassNode;
import ca.mcgill.cs.stg.jetuml.graph.ClassRelationshipEdge;
import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.FieldNode;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.ImplicitParameterNode;
import ca.mcgill.cs.stg.jetuml.graph.InterfaceNode;
import ca.mcgill.cs.stg.jetuml.graph.Node;
import ca.mcgill.cs.stg.jetuml.graph.NoteEdge;
import ca.mcgill.cs.stg.jetuml.graph.NoteNode;
import ca.mcgill.cs.stg.jetuml.graph.ObjectNode;
import ca.mcgill.cs.stg.jetuml.graph.ObjectReferenceEdge;
import ca.mcgill.cs.stg.jetuml.graph.PackageNode;
import ca.mcgill.cs.stg.jetuml.graph.PointNode;
import ca.mcgill.cs.stg.jetuml.graph.ReturnEdge;
import ca.mcgill.cs.stg.jetuml.graph.StateNode;
import ca.mcgill.cs.stg.jetuml.graph.StateTransitionEdge;

public class TestPersistenceService
{
	private static final String TEST_FILE_NAME = "testdata/tmp";
	
	@Test
	public void testClassDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.class.jet"));
		verifyClassDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyClassDiagram(graph);
		tmp.delete();
	}
	
	@Test
	public void testClassDiagramContainment() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService2.class.jet"));
		verifyClassDiagram2(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyClassDiagram2(graph);
		tmp.delete();
	}
	
	@Test
	public void testStateDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.state.jet"));
		verifyStateDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyStateDiagram(graph);
		tmp.delete();
	}
	
	@Test
	public void testObjectDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.object.jet"));
		verifyObjectDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyObjectDiagram(graph);
		tmp.delete();
	}
	
	private void verifyClassDiagram2(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(8, nodes.size());
		Iterator<Node> nIterator = nodes.iterator();
		PackageNode p1 = (PackageNode) nIterator.next();
		ClassNode c1 = (ClassNode) nIterator.next();
		PackageNode p2 = (PackageNode) nIterator.next();
		NoteNode n1 = (NoteNode) nIterator.next();
		PackageNode p3 = (PackageNode) nIterator.next();
		PackageNode p4 = (PackageNode) nIterator.next();
		InterfaceNode i1 = (InterfaceNode) nIterator.next();
		ClassNode c2 = (ClassNode) nIterator.next();
		
		List<Node> children = p1.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(c1));
		assertEquals(p1, c1.getParent());
		assertEquals(0, c1.getChildren().size());
		
		children = p2.getChildren();
		assertEquals(0, children.size());
		assertNull(n1.getParent());
		
		children = p3.getChildren();
		assertEquals(2, children.size());
		assertTrue(children.contains(p4));
		assertTrue(children.contains(c2));
		assertEquals(p3, p4.getParent());
		assertEquals(p3, c2.getParent());
		
		children = p4.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(i1));
		assertEquals(p4, i1.getParent());
	}
	
	private void verifyClassDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		
		assertEquals(8, nodes.size());
		Iterator<Node> nIterator = nodes.iterator();
		
		ClassNode node1 = (ClassNode) nIterator.next();
		InterfaceNode node2 = (InterfaceNode) nIterator.next();
		ClassNode node3 = (ClassNode) nIterator.next();
		ClassNode node4 = (ClassNode) nIterator.next();
		NoteNode node5 = (NoteNode) nIterator.next();
		PackageNode node6 = (PackageNode) nIterator.next();
		ClassNode node7 = (ClassNode) nIterator.next();
		PointNode node8 = (PointNode) nIterator.next();
		
		assertEquals("", node1.getAttributes().getText());
		assertTrue(node1.getChildren().isEmpty());
		assertEquals("", node1.getMethods().getText());
		assertEquals("Class1", node1.getName().getText());
		assertNull(node1.getParent());
		assertEquals(new Rectangle2D.Double(470, 180, 100, 60), node1.getBounds());
		
		assertTrue(node2.getChildren().isEmpty());
		assertEquals("", node2.getMethods().getText());
		assertEquals("�interface�", node2.getName().getText());
		assertNull(node2.getParent());
		assertEquals(new Rectangle2D.Double(470, 70, 100, 60), node2.getBounds());
		
		assertEquals("foo", node3.getAttributes().getText());
		assertTrue(node3.getChildren().isEmpty());
		assertEquals("bar", node3.getMethods().getText());
		assertEquals("Class2", node3.getName().getText());
		assertNull(node3.getParent());
		assertEquals(new Rectangle2D.Double(470, 310, 100, 60), node3.getBounds());
		
		assertEquals("", node4.getAttributes().getText());
		assertTrue(node4.getChildren().isEmpty());
		assertEquals("", node4.getMethods().getText());
		assertEquals("Class3", node4.getName().getText());
		assertNull(node4.getParent());
		assertEquals(new Rectangle2D.Double(660, 180, 100, 60), node4.getBounds());
		
		assertTrue(node5.getChildren().isEmpty());
		assertEquals("A note", node5.getText().getText());
		assertNull(node5.getParent());
		assertEquals(new Rectangle2D.Double(770, 310, 60, 40), node5.getBounds());
		
		List<Node> children = node6.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node7));
		assertEquals("", node6.getContents().getText());
		assertEquals("Package", node6.getName());
		assertNull(node6.getParent());
		assertEquals(new Rectangle2D.Double(220, 110, 160, 120), node6.getBounds());

		assertEquals("", node7.getAttributes().getText());
		assertTrue(node7.getChildren().isEmpty());
		assertEquals("", node7.getMethods().getText());
		assertEquals("Class", node7.getName().getText());
		assertEquals(node6,node7.getParent());
		assertEquals(new Rectangle2D.Double(260, 160, 100, 60), node7.getBounds());
		
		assertEquals(new Rectangle2D.Double(708, 229, 0, 0), node8.getBounds());
		assertTrue(node8.getChildren().isEmpty());
		assertNull(node8.getParent());
		
		Collection<Edge> edges = pGraph.getEdges();
		assertEquals(6, edges.size());
		Iterator<Edge> eIterator = edges.iterator();
		ClassRelationshipEdge edge1 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("VHV", edge1.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(515, 130, 22, 50), edge1.getBounds());
		assertEquals(node2, edge1.getEnd());
		assertEquals("TRIANGLE", edge1.getEndArrowHead().toString());
		assertEquals("", edge1.getEndLabel());
		assertEquals("DOTTED", edge1.getLineStyle().toString());
		assertEquals("e2", edge1.getMiddleLabel());
		assertEquals(node1, edge1.getStart());
		assertEquals("NONE", edge1.getStartArrowHead().toString());
		assertEquals("", edge1.getStartLabel());
		
		ClassRelationshipEdge edge2 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("VHV", edge2.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(515, 240, 22, 70), edge2.getBounds());
		assertEquals(node1, edge2.getEnd());
		assertEquals("TRIANGLE", edge2.getEndArrowHead().toString());
		assertEquals("", edge2.getEndLabel());
		assertEquals("SOLID", edge2.getLineStyle().toString());
		assertEquals("e3", edge2.getMiddleLabel());
		assertEquals(node3, edge2.getStart());
		assertEquals("NONE", edge2.getStartArrowHead().toString());
		assertEquals("", edge2.getStartLabel());
		
		ClassRelationshipEdge edge3 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("HVH", edge3.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(570, 191, 90, 24), edge3.getBounds());
		assertEquals(node4, edge3.getEnd());
		assertEquals("NONE", edge3.getEndArrowHead().toString());
		assertEquals("*", edge3.getEndLabel());
		assertEquals("SOLID", edge3.getLineStyle().toString());
		assertEquals("e4", edge3.getMiddleLabel());
		assertEquals(node1, edge3.getStart());
		assertEquals("DIAMOND", edge3.getStartArrowHead().toString());
		assertEquals("1", edge3.getStartLabel());
		
		ClassRelationshipEdge edge4 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("HVH", edge4.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(570, 205, 90, 135), edge4.getBounds());
		assertEquals(node3, edge4.getEnd());
		assertEquals("NONE", edge4.getEndArrowHead().toString());
		assertEquals("", edge4.getEndLabel());
		assertEquals("SOLID", edge4.getLineStyle().toString());
		assertEquals("e5", edge4.getMiddleLabel());
		assertEquals(node4, edge4.getStart());
		assertEquals("BLACK_DIAMOND", edge4.getStartArrowHead().toString());
		assertEquals("", edge4.getStartLabel());
		
		NoteEdge edge5 = (NoteEdge) eIterator.next();
		assertEquals(new Rectangle2D.Double(708, 229, 74, 81), edge5.getBounds());
		assertEquals(node5, edge5.getStart());
		assertEquals(node8, edge5.getEnd());
		
		ClassRelationshipEdge edge6 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("Straight", edge6.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(360, 181, 110, 29), edge6.getBounds());
		assertEquals(node7, edge6.getEnd());
		assertEquals("V", edge6.getEndArrowHead().toString());
		assertEquals("", edge6.getEndLabel());
		assertEquals("DOTTED", edge6.getLineStyle().toString());
		assertEquals("e1", edge6.getMiddleLabel());
		assertEquals(node1, edge6.getStart());
		assertEquals("NONE", edge6.getStartArrowHead().toString());
		assertEquals("", edge6.getStartLabel());
	}
	
	@Test
	public void testSequenceDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.sequence.jet"));
		verifySequenceDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifySequenceDiagram(graph);
		tmp.delete();
	}
	
	private void verifySequenceDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(9, nodes.size());
		Iterator<Node> nIterator = nodes.iterator();
		ImplicitParameterNode node1 = (ImplicitParameterNode) nIterator.next();
		CallNode node2 = (CallNode) nIterator.next();
		CallNode node3 = (CallNode) nIterator.next();
		ImplicitParameterNode node4 = (ImplicitParameterNode) nIterator.next();
		CallNode node5 = (CallNode) nIterator.next();
		ImplicitParameterNode node6 = (ImplicitParameterNode) nIterator.next();
		CallNode node7 = (CallNode) nIterator.next();
		NoteNode node8 = (NoteNode) nIterator.next();
		PointNode node9 = (PointNode) nIterator.next();
		
		assertEquals(new Rectangle2D.Double(210, 0, 100, 215), node1.getBounds());
		assertEquals(0, node1.getChildren().size());
		assertEquals("object1:Type1", node1.getName().toString());
		assertNull(node1.getParent());
		
		assertEquals(new Rectangle2D.Double(252, 73, 16, 30), node2.getBounds());
		List<Node> children = node2.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node3));
		assertEquals(node1, node2.getImplicitParameter());
		assertNull(node2.getParent());
		
		// In sequence diagram call nodes have a children the call
		// nodes they call
		assertEquals(new Rectangle2D.Double(260, 102, 16, 30), node3.getBounds());
		children = node3.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node5));
		assertEquals(node1, node3.getImplicitParameter());
		assertEquals(node2, node3.getParent());
		
		assertEquals(new Rectangle2D.Double(500, 0, 80, 215), node4.getBounds());
		assertEquals(0, node4.getChildren().size());
		assertEquals(":Type2", node4.getName().toString());
		assertNull(node4.getParent());
		
		assertEquals(new Rectangle2D.Double(532, 121, 16, 30), node5.getBounds());
		children = node5.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node7));
		assertEquals(node4, node5.getImplicitParameter());
		assertEquals(node3, node5.getParent());
		
		assertEquals(new Rectangle2D.Double(640, 0, 80, 215), node6.getBounds());
		assertEquals(0, node6.getChildren().size());
		assertEquals("object3:", node6.getName().toString());
		assertNull(node6.getParent());
		
		assertEquals(new Rectangle2D.Double(672, 145, 16, 30), node7.getBounds());
		children = node7.getChildren();
		assertEquals(0, children.size());
		assertEquals(node6, node7.getImplicitParameter());
		assertEquals(node5, node7.getParent());
		
		assertTrue(node8.getChildren().isEmpty());
		assertEquals("A note", node8.getText().getText());
		assertNull(node8.getParent());
		assertEquals(new Rectangle2D.Double(610, 210, 60, 40), node8.getBounds());
		
		assertEquals(new Rectangle2D.Double(538, 169, 0, 0), node9.getBounds());
		assertTrue(node9.getChildren().isEmpty());
		assertNull(node9.getParent());
		
		Collection<Edge> edges = pGraph.getEdges();
		assertEquals(6, edges.size());
		Iterator<Edge> eIterator = edges.iterator();
		
		CallEdge self = (CallEdge) eIterator.next(); 
		CallEdge signal = (CallEdge) eIterator.next(); 
		ReturnEdge retS = (ReturnEdge) eIterator.next(); 
		CallEdge call1 = (CallEdge) eIterator.next(); 
		ReturnEdge retC = (ReturnEdge) eIterator.next(); 
		NoteEdge note = (NoteEdge) eIterator.next(); 
		
		assertEquals(new Rectangle2D.Double(268, 78, 77, 29), self.getBounds());
		assertEquals(node3, self.getEnd());
		assertEquals("V", self.getEndArrowHead().toString());
		assertEquals("", self.getEndLabel());
		assertEquals("SOLID", self.getLineStyle().toString());
		assertEquals("selfCall()", self.getMiddleLabel());
		assertEquals(node2, self.getStart());
		assertEquals("NONE", self.getStartArrowHead().toString());
		assertEquals("", self.getStartLabel());
		assertFalse(self.isSignal());
		
		assertEquals(new Rectangle2D.Double(276, 102, 256, 19), signal.getBounds());
		assertEquals(node5, signal.getEnd());
		assertEquals("HALF_V", signal.getEndArrowHead().toString());
		assertTrue(signal.isSignal());
		assertEquals("", signal.getEndLabel());
		assertEquals("SOLID", signal.getLineStyle().toString());
		assertEquals("signal", signal.getMiddleLabel());
		assertEquals(node3, signal.getStart());
		assertEquals("NONE", signal.getStartArrowHead().toString());
		assertEquals("", signal.getStartLabel());
		
		assertEquals(new Rectangle2D.Double(276, 146, 256, 10), retS.getBounds());
		assertEquals(node3, retS.getEnd());
		assertEquals("V", retS.getEndArrowHead().toString());
		assertEquals("", retS.getEndLabel());
		assertEquals("DOTTED", retS.getLineStyle().toString());
		assertEquals("", retS.getMiddleLabel());
		assertEquals(node5, retS.getStart());
		assertEquals("NONE", retS.getStartArrowHead().toString());
		assertEquals("", retS.getStartLabel());
		
		assertEquals(new Rectangle2D.Double(548, 126, 124, 24), call1.getBounds());
		assertEquals(node7, call1.getEnd());
		assertEquals("V", call1.getEndArrowHead().toString());
		assertEquals("", call1.getEndLabel());
		assertEquals("SOLID", call1.getLineStyle().toString());
		assertEquals("call1", call1.getMiddleLabel());
		assertEquals(node5, call1.getStart());
		assertEquals("NONE", call1.getStartArrowHead().toString());
		assertEquals("", call1.getStartLabel());
		assertFalse(call1.isSignal());
		
		assertEquals(new Rectangle2D.Double(548, 156, 124, 24), retC.getBounds());
		assertEquals(node5, retC.getEnd());
		assertEquals("V", retC.getEndArrowHead().toString());
		assertEquals("", retC.getEndLabel());
		assertEquals("DOTTED", retC.getLineStyle().toString());
		assertEquals("r1", retC.getMiddleLabel());
		assertEquals(node7, retC.getStart());
		assertEquals("NONE", retC.getStartArrowHead().toString());
		assertEquals("", retC.getStartLabel());
		
		assertEquals(new Rectangle2D.Double(538, 169, 72, 44), note.getBounds());
		assertEquals(node8, note.getStart());
		assertEquals(node9, note.getEnd());
	}
	
	private void verifyStateDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(7, nodes.size());
		
		Iterator<Node> nIterator = nodes.iterator();
		StateNode s1 = (StateNode) nIterator.next(); 
		StateNode s2 = (StateNode) nIterator.next(); 
		StateNode s3 = (StateNode) nIterator.next(); 
		CircularStateNode start = (CircularStateNode) nIterator.next(); 
		CircularStateNode end = (CircularStateNode) nIterator.next(); 
		NoteNode note = (NoteNode) nIterator.next();
		PointNode point = (PointNode) nIterator.next();
		
		assertEquals(new Rectangle2D.Double(250, 100, 80, 60), s1.getBounds());
		assertTrue(s1.getChildren().isEmpty());
		assertEquals("S1", s1.getName().toString());
		assertNull(s1.getParent());
		
		assertEquals(new Rectangle2D.Double(510, 100, 80, 60), s2.getBounds());
		assertTrue(s2.getChildren().isEmpty());
		assertEquals("S2", s2.getName().toString());
		assertNull(s2.getParent());
		
		assertEquals(new Rectangle2D.Double(520, 310, 80, 60), s3.getBounds());
		assertTrue(s3.getChildren().isEmpty());
		assertEquals("S3", s3.getName().toString());
		assertNull(s3.getParent());
		
		assertEquals(new Rectangle2D.Double(150, 70, 40, 40), start.getBounds());
		assertTrue(start.getChildren().isEmpty());
		assertNull(start.getParent());
		assertFalse(start.isFinal());
		
		assertEquals(new Rectangle2D.Double(640, 230, 38, 38), end.getBounds());
		assertTrue(end.getChildren().isEmpty());
		assertNull(end.getParent());
		assertTrue(end.isFinal());
		
		assertTrue(note.getChildren().isEmpty());
		assertEquals("A note\non two lines", note.getText().getText());
		assertNull(note.getParent());
		assertEquals(new Rectangle2D.Double(690, 320, 80, 40), note.getBounds());
		
		assertEquals(new Rectangle2D.Double(576, 339, 0, 0), point.getBounds());
		assertTrue(point.getChildren().isEmpty());
		assertNull(point.getParent());
		
		Collection<Edge> edges = pGraph.getEdges();
		assertEquals(7, edges.size());
		Iterator<Edge> eIterator = edges.iterator();
		
		NoteEdge ne = (NoteEdge) eIterator.next();
		StateTransitionEdge fromStart = (StateTransitionEdge) eIterator.next(); 
		StateTransitionEdge e1 = (StateTransitionEdge) eIterator.next(); 
		StateTransitionEdge e2 = (StateTransitionEdge) eIterator.next(); 
		StateTransitionEdge self = (StateTransitionEdge) eIterator.next(); 
		StateTransitionEdge toEnd = (StateTransitionEdge) eIterator.next(); 
		StateTransitionEdge toS3 = (StateTransitionEdge) eIterator.next(); 
		
		assertEquals(new Rectangle2D.Double(576, 339, 114, 1), ne.getBounds());
		assertEquals(note, ne.getStart());
		assertEquals(point, ne.getEnd());
		
		assertEquals(new Rectangle2D.Double(189, 81, 62, 32), fromStart.getBounds());
		assertEquals(start, fromStart.getStart());
		assertEquals(s1, fromStart.getEnd());
		assertEquals("start", fromStart.getLabel().toString());
		
		assertEquals(new Rectangle2D.Double(330, 99, 180, 28), e1.getBounds());
		assertEquals(s1, e1.getStart());
		assertEquals(s2, e1.getEnd());
		assertEquals("e1", e1.getLabel().toString());
		
		assertEquals(new Rectangle2D.Double(330, 133, 180, 28), e2.getBounds());
		assertEquals(s2, e2.getStart());
		assertEquals(s1, e2.getEnd());
		assertEquals("e2", e2.getLabel().toString());
		
		assertEquals(new Rectangle2D.Double(590, 106, 44, 48), self.getBounds());
		assertEquals(s2, self.getStart());
		assertEquals(s2, self.getEnd());
		assertEquals("self", self.getLabel().toString());
		
		assertEquals(new Rectangle2D.Double(587, 260, 57, 50), toEnd.getBounds());
		assertEquals(s3, toEnd.getStart());
		assertEquals(end, toEnd.getEnd());
		assertEquals("", toEnd.getLabel().toString());
		
		assertEquals(new Rectangle2D.Double(554, 160, 17, 150), toS3.getBounds());
		assertEquals(s2, toS3.getStart());
		assertEquals(s3, toS3.getEnd());
		assertEquals("", toS3.getLabel().toString());
	}
	
	private void verifyObjectDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(11, nodes.size());
		
		Iterator<Node> nIt = nodes.iterator(); 
		ObjectNode type1 = (ObjectNode) nIt.next(); 
		ObjectNode blank = (ObjectNode) nIt.next(); 
		FieldNode name = (FieldNode) nIt.next(); 
		ObjectNode object2 = (ObjectNode) nIt.next(); 
		FieldNode name2 = (FieldNode) nIt.next(); 
		ObjectNode type3 = (ObjectNode) nIt.next();
		FieldNode name3 = (FieldNode) nIt.next(); 
		FieldNode name4 = (FieldNode) nIt.next(); 
		NoteNode note = (NoteNode) nIt.next(); 
		PointNode p1 = (PointNode) nIt.next();
		PointNode p2 = (PointNode) nIt.next();
		
		assertEquals(new Rectangle2D.Double(210, 70, 120, 100), type1.getBounds());
		assertEquals(1, type1.getChildren().size());
		assertTrue(type1.getChildren().contains(name));
		assertEquals(":Type1", type1.getName().toString());
		assertNull(type1.getParent());
		
		assertEquals(new Rectangle2D.Double(370, 170, 160, 140), blank.getBounds());
		List<Node> children = blank.getChildren();
		assertEquals(3, children.size());
		assertTrue(children.contains(name2));
		assertTrue(children.contains(name3));
		assertTrue(children.contains(name4));
		assertEquals("", blank.getName().toString());
		assertNull(blank.getParent());
		
		assertEquals(new Rectangle2D.Double(222.5, 149, 87, 16), name.getBounds());
		children = name.getChildren();
		assertEquals(0, children.size());
		assertEquals(0,name.getAxisX(),0.000001);
		assertEquals("name", name.getName().toString());
		assertEquals(type1, name.getObjectNode());
		assertEquals(type1, name.getParent());
		assertEquals("", name.getValue().toString());
		assertFalse(name.isBoxedValue());
		
		assertEquals(new Rectangle2D.Double(480, 80, 80, 60), object2.getBounds());
		children = object2.getChildren();
		assertEquals(0, children.size());
		assertEquals("object2:", object2.getName().toString());
		assertNull(object2.getParent());
		
		assertEquals(new Rectangle2D.Double(395.5, 247, 122, 16), name2.getBounds());
		children = name2.getChildren();
		assertEquals(0, children.size());
		assertEquals(0,name2.getAxisX(),0.000001);
		assertEquals("name2", name2.getName().toString());
		assertEquals(blank, name2.getObjectNode());
		assertEquals(blank, name2.getParent());
		assertEquals("test value", name2.getValue().toString());
		assertFalse(name2.isBoxedValue());
		
		assertEquals(new Rectangle2D.Double(570, 190, 80, 60), type3.getBounds());
		children = type3.getChildren();
		assertEquals(0, children.size());
		assertEquals(":Type3", type3.getName().toString());
		assertNull(type3.getParent());
		
		assertEquals(new Rectangle2D.Double(395.5, 268, 122, 16), name3.getBounds());
		children = name3.getChildren();
		assertEquals(0, children.size());
		assertEquals(0,name3.getAxisX(),0.000001);
		assertEquals("name3", name3.getName().toString());
		assertEquals(blank, name3.getObjectNode());
		assertEquals(blank, name3.getParent());
		assertEquals("value", name3.getValue().toString());
		assertTrue(name3.isBoxedValue());
		
		assertEquals(new Rectangle2D.Double(395.5, 289, 122, 16), name4.getBounds());
		children = name4.getChildren();
		assertEquals(0, children.size());
		assertEquals(0,name4.getAxisX(),0.000001);
		assertEquals("name4", name4.getName().toString());
		assertEquals(blank, name4.getObjectNode());
		assertEquals(blank, name4.getParent());
		assertEquals("", name4.getValue().toString());
		assertFalse(name4.isBoxedValue());
		
		assertTrue(note.getChildren().isEmpty());
		assertEquals("A note", note.getText().getText());
		assertNull(note.getParent());
		assertEquals(new Rectangle2D.Double(220, 220, 60, 40), note.getBounds());
		
		assertEquals(new Rectangle2D.Double(388, 225, 0, 0), p1.getBounds());
		assertTrue(p1.getChildren().isEmpty());
		assertNull(p1.getParent());
		
		assertEquals(new Rectangle2D.Double(249, 162, 0, 0), p2.getBounds());
		assertTrue(p2.getChildren().isEmpty());
		assertNull(p2.getParent());
		
		Collection<Edge> edges = pGraph.getEdges();
		assertEquals(6, edges.size());
		Iterator<Edge> eIt = edges.iterator();
		ObjectReferenceEdge o1 = (ObjectReferenceEdge) eIt.next();
		ObjectReferenceEdge o2 = (ObjectReferenceEdge) eIt.next();
		ClassRelationshipEdge cr1 = (ClassRelationshipEdge) eIt.next();
		ObjectReferenceEdge o3 = (ObjectReferenceEdge) eIt.next();
		NoteEdge ne1 = (NoteEdge) eIt.next();
		NoteEdge ne2 = (NoteEdge) eIt.next();
		
		assertEquals(new Rectangle2D.Double(266, 70, 84, 87), o1.getBounds());
		assertEquals(name, o1.getStart());
		assertEquals(type1, o1.getEnd());
		
		assertEquals(new Rectangle2D.Double(266, 157, 104, 13), o2.getBounds());
		assertEquals(name, o2.getStart());
		assertEquals(blank, o2.getEnd());
		
		assertEquals(new Rectangle2D.Double(450, 110, 32, 60), cr1.getBounds());
		assertEquals("Straight", cr1.getBentStyle().toString());
		assertEquals(object2, cr1.getEnd());
		assertEquals("NONE", cr1.getEndArrowHead().toString());
		assertEquals("", cr1.getEndLabel());
		assertEquals("SOLID", cr1.getLineStyle().toString());
		assertEquals("e1", cr1.getMiddleLabel().toString());
		assertEquals(blank, cr1.getStart());
		assertEquals("NONE", cr1.getStartArrowHead().toString());
		assertEquals("", cr1.getStartLabel().toString());
		
		assertEquals(new Rectangle2D.Double(456, 190, 114, 107), o3.getBounds());
		assertEquals(name4, o3.getStart());
		assertEquals(type3, o3.getEnd());
		
		assertEquals(new Rectangle2D.Double(280, 225, 108, 12), ne1.getBounds());
		assertEquals(note, ne1.getStart());
		assertEquals(p1, ne1.getEnd());
		
		assertEquals(new Rectangle2D.Double(249, 162, 1, 58), ne2.getBounds());
		assertEquals(note, ne2.getStart());
		assertEquals(p2, ne2.getEnd());
	}
}
