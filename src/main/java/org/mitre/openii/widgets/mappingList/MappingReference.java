package org.mitre.openii.widgets.mappingList;

import org.mitre.schemastore.model.Mapping;
import org.mitre.schemastore.model.Schema;

import java.util.List;

/** Class for defining a mapping reference */ @SuppressWarnings("serial")
class MappingReference extends Mapping
{
	/** Stores the mapping label */
	private String label;
	
	/** Constructs the mapping */
	MappingReference(Mapping mapping, List<Schema> schemas)
	{
		// Stores the mapping
		super(mapping.getId(),mapping.getProjectId(),mapping.getSourceId(),mapping.getTargetId());
		
		// Generate the label
		String source=null, target=null;
		for(Schema schema : schemas)
		{
			if(schema.getId().equals(getSourceId())) source=schema.getName();
			if(schema.getId().equals(getTargetId())) target=schema.getName();
		}
		
		label = "'" + source + "' to '" + target + "'";
	}

	/** Outputs the mapping reference */
	public String toString()
		{ return label; }
}
