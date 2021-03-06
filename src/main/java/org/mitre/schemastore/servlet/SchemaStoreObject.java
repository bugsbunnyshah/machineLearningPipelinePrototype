/**
 * SchemaStore.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.mitre.schemastore.servlet;

public interface SchemaStoreObject extends java.rmi.Remote {
    public org.mitre.schemastore.model.Domain getDomain(int domainID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Mapping[] getMappings(int projectID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Project getProject(int projectID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Project[] getProjects() throws java.rmi.RemoteException;
    public boolean deleteProject(int projectID) throws java.rmi.RemoteException;
    public boolean compress() throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Tag[] getTags() throws java.rmi.RemoteException;
    public boolean setAnnotations(org.mitre.schemastore.model.Annotation[] annotations) throws java.rmi.RemoteException;
    public boolean setAnnotation(int elementID, int groupID, String attribute, String value) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Relationship getRelationship(int relationshipID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Entity getEntity(int entityID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Function getFunction(int functionID) throws java.rmi.RemoteException;
    public boolean updateAttribute(org.mitre.schemastore.model.Attribute attribute) throws java.rmi.RemoteException;
    public int addSubtype(org.mitre.schemastore.model.Subtype subtype) throws java.rmi.RemoteException;
    public int addProject(org.mitre.schemastore.model.Project project) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Function[] getFunctions() throws java.rmi.RemoteException;
    public int addSchema(org.mitre.schemastore.model.Schema schema) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Schema[] getSchemas() throws java.rmi.RemoteException;
    public int importSchema(org.mitre.schemastore.model.Schema schema, org.mitre.schemastore.model.SchemaElementList schemaElementList) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.DataSource getDataSource(int dataSourceID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Schema extendSchema(int schemaID) throws java.rmi.RemoteException;
    public boolean updateSchema(org.mitre.schemastore.model.Schema schema) throws java.rmi.RemoteException;
    public boolean unlockSchema(int schemaID) throws java.rmi.RemoteException;
    public boolean lockSchema(int schemaID) throws java.rmi.RemoteException;
    public boolean isDeletable(int schemaID) throws java.rmi.RemoteException;
    public int[] getDeletableSchemas() throws java.rmi.RemoteException;
    public boolean deleteSchema(int schemaID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Thesaurus[] getThesauri() throws java.rmi.RemoteException;
    public int addThesaurus(org.mitre.schemastore.model.Thesaurus thesaurus) throws java.rmi.RemoteException;
    public boolean updateThesaurus(org.mitre.schemastore.model.Thesaurus thesaurus) throws java.rmi.RemoteException;
    public boolean deleteThesaurus(int thesaurusID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Tag[] getSubcategories(int tagID) throws java.rmi.RemoteException;
    public int addTag(org.mitre.schemastore.model.Tag tag) throws java.rmi.RemoteException;
    public boolean updateTag(org.mitre.schemastore.model.Tag tag) throws java.rmi.RemoteException;
    public boolean deleteTag(int tagID) throws java.rmi.RemoteException;
    public int[] getTagSchemas(int tagID) throws java.rmi.RemoteException;
    public int[] getSchemaTags(int schemaID) throws java.rmi.RemoteException;
    public boolean addTagToSchema(int schemaID, int tagID) throws java.rmi.RemoteException;
    public boolean removeTagFromSchema(int schemaID, int tagID) throws java.rmi.RemoteException;
    public int[] getParentSchemas(int schemaID) throws java.rmi.RemoteException;
    public int[] getChildSchemas(int schemaID) throws java.rmi.RemoteException;
    public int[] getAncestorSchemas(int schemaID) throws java.rmi.RemoteException;
    public int[] getDescendantSchemas(int schemaID) throws java.rmi.RemoteException;
    public int[] getAssociatedSchemas(int schemaID) throws java.rmi.RemoteException;
    public int getRootSchema(int schema1ID, int schema2ID) throws java.rmi.RemoteException;
    public int[] getSchemaPath(int rootID, int schemaID) throws java.rmi.RemoteException;
    public boolean setParentSchemas(int schemaID, int[] parentIDs) throws java.rmi.RemoteException;
    public int addEntity(org.mitre.schemastore.model.Entity entity) throws java.rmi.RemoteException;
    public int addDomain(org.mitre.schemastore.model.Domain domain) throws java.rmi.RemoteException;
    public int addDomainValue(org.mitre.schemastore.model.DomainValue domainValue) throws java.rmi.RemoteException;
    public int addRelationship(org.mitre.schemastore.model.Relationship relationship) throws java.rmi.RemoteException;
    public int addContainment(org.mitre.schemastore.model.Containment containment) throws java.rmi.RemoteException;
    public int addSynonym(org.mitre.schemastore.model.Synonym synonym) throws java.rmi.RemoteException;
    public boolean updateEntity(org.mitre.schemastore.model.Entity entity) throws java.rmi.RemoteException;
    public boolean updateDomain(org.mitre.schemastore.model.Domain domain) throws java.rmi.RemoteException;
    public boolean updateDomainValue(org.mitre.schemastore.model.DomainValue domainValue) throws java.rmi.RemoteException;
    public boolean updateRelationship(org.mitre.schemastore.model.Relationship relationship) throws java.rmi.RemoteException;
    public boolean updateContainment(org.mitre.schemastore.model.Containment containment) throws java.rmi.RemoteException;
    public boolean updateSubtype(org.mitre.schemastore.model.Subtype subtype) throws java.rmi.RemoteException;
    public boolean updateSynonym(org.mitre.schemastore.model.Synonym synonym) throws java.rmi.RemoteException;
    public boolean updateAlias(org.mitre.schemastore.model.Alias alias) throws java.rmi.RemoteException;
    public boolean deleteEntity(int entityID) throws java.rmi.RemoteException;
    public boolean deleteAttribute(int attributeID) throws java.rmi.RemoteException;
    public boolean deleteDomain(int domainID) throws java.rmi.RemoteException;
    public boolean deleteDomainValue(int domainValueID) throws java.rmi.RemoteException;
    public boolean deleteRelationship(int relationshipID) throws java.rmi.RemoteException;
    public boolean deleteContainment(int containmentID) throws java.rmi.RemoteException;
    public boolean deleteSubtype(int subtypeID) throws java.rmi.RemoteException;
    public boolean deleteSynonym(int synonymID) throws java.rmi.RemoteException;
    public boolean deleteAlias(int aliasID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.DomainValue getDomainValue(int domainValueID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Containment getContainment(int containmentID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Subtype getSubtype(int subtypeID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Synonym getSynonym(int synonymID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Alias getAlias(int aliasID) throws java.rmi.RemoteException;
    public int getSchemaElementCount(int schemaID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.SchemaElementList getSchemaElements(int schemaID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.SchemaElementList getSchemaElementsForKeyword(String keyword, int[] tagIDs) throws java.rmi.RemoteException;
    public String getSchemaElementType(int schemaElementID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.DataSource[] getAllDataSources() throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.DataSource[] getDataSources(int schemaID) throws java.rmi.RemoteException;
    public int addDataSource(org.mitre.schemastore.model.DataSource dataSource) throws java.rmi.RemoteException;
    public boolean updateDataSource(org.mitre.schemastore.model.DataSource dataSource) throws java.rmi.RemoteException;
    public boolean deleteDataSource(int dataSourceID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.DataType[] getDataTypes() throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Function[] getReferencedFunctions(int functionID) throws java.rmi.RemoteException;
    public int addFunction(org.mitre.schemastore.model.Function function) throws java.rmi.RemoteException;
    public int[] getDeletableFunctions() throws java.rmi.RemoteException;
    public boolean deleteFunction(int functionID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.FunctionImp[] getAllFunctionImps() throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.FunctionImp[] getFunctionImps(int functionID) throws java.rmi.RemoteException;
    public boolean setFunctionImp(org.mitre.schemastore.model.FunctionImp functionImp) throws java.rmi.RemoteException;
    public boolean deleteFunctionImp(org.mitre.schemastore.model.FunctionImp functionImp) throws java.rmi.RemoteException;
    public boolean updateProject(org.mitre.schemastore.model.Project project) throws java.rmi.RemoteException;
    public boolean deleteMapping(int mappingID) throws java.rmi.RemoteException;
    public int addMappingCells(org.mitre.schemastore.model.MappingCell[] mappingCells) throws java.rmi.RemoteException;
    public boolean updateMappingCells(org.mitre.schemastore.model.MappingCell[] mappingCells) throws java.rmi.RemoteException;
    public boolean deleteMappingCells(int[] mappingCellIDs) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.MappingCell[] getMappingCells(int mappingID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.MappingCell[] getMappingCellsByElement(int projectID, org.mitre.schemastore.model.terms.AssociatedElement[] elements, double minScore) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.MappingCell[] getAssociatedMappingCells(int projectID, org.mitre.schemastore.model.terms.AssociatedElement[] elements) throws java.rmi.RemoteException;
    public boolean hasVocabulary(int projectID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Annotation[] getAnnotationsByGroup(int groupID, String attribute) throws java.rmi.RemoteException;
    public boolean clearAnnotation(int elementID, int groupID, String attribute) throws java.rmi.RemoteException;
    public boolean clearAnnotations(int groupID, String attribute) throws java.rmi.RemoteException;
    public boolean saveMappingCells(int mappingID, org.mitre.schemastore.model.MappingCell[] mappingCells) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.terms.VocabularyTerms getVocabulary(int projectID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.terms.VocabularyTerms saveVocabulary(org.mitre.schemastore.model.terms.VocabularyTerms vocabulary) throws java.rmi.RemoteException;
    public boolean deleteVocabulary(int projectID) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.terms.ThesaurusTerms getThesaurusTerms(int thesaurusID) throws java.rmi.RemoteException;
    public boolean saveThesaurusTerms(org.mitre.schemastore.model.terms.ThesaurusTerms terms) throws java.rmi.RemoteException;
    public String getAnnotation(int elementID, int groupID, String attribute) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Annotation[] getAnnotations(int elementID, String attribute) throws java.rmi.RemoteException;
    public int addAttribute(org.mitre.schemastore.model.Attribute attribute) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Attribute getAttribute(int attributeID) throws java.rmi.RemoteException;
    public boolean isConnected() throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Schema getSchema(int schemaID) throws java.rmi.RemoteException;
    public int addMapping(org.mitre.schemastore.model.Mapping mapping) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Mapping getMapping(int mappingID) throws java.rmi.RemoteException;
    public int addAlias(org.mitre.schemastore.model.Alias alias) throws java.rmi.RemoteException;
    public org.mitre.schemastore.model.Tag getTag(int tagID) throws java.rmi.RemoteException;
}
