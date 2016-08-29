package org.modelio.parmalpha.command;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of the IModuleContextualCommand interface.
 * <br>The module contextual commands are displayed in the contextual menu and in the specific toolbar of each module property page.
 * <br>The developer may inherit the DefaultModuleContextualCommand class which contains a default standard contextual command implementation.
 *
 */
public class SetVisibilityCommand extends DefaultModuleCommandHandler {
    /**
     * Constructor.
     */
    public SetVisibilityCommand() {
        super();
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
        // Check that there is only one selected element
        return selectedElements.size() == 1;
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @SuppressWarnings("deprecation")
	@Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
		IModelingSession session = module.getModuleContext().getModelingSession();
		ITransaction t = session.createTransaction("Mask element");	
		try {
			IDiagramService diagramServices = Modelio.getInstance().getDiagramService(); 
			ModelElement modelelt = (ModelElement)selectedElements.get(0);
			for (AbstractDiagram ad: modelelt.getDiagramElement()){
				IDiagramHandle dh = diagramServices.getDiagramHandle(ad);
				// loop on the top level diagram graphics
				for (IDiagramGraphic dg: dh.getDiagramNode().getNodes()) {
					MObject obj = dg.getElement();
				    if (obj.getName().equalsIgnoreCase(modelelt.getName())){
				    	MessageDialog.openInformation(null, "Masking", obj.getName());
				    	dg.mask();
				    }
				}
			}
    		t.commit();
		} catch (Exception e) {
		t.rollback();
		}
        
    }


}
