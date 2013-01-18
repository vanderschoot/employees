package org.biz.employees.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class AbstractDAO< T > {
	protected Class<T> classT;

	@PersistenceContext
	private EntityManager em;
	    	   
	public void setClassT( final Class<T> classT ){
	   this.classT = classT;
	}
	
    public T findById(final int id) {
		final T e =  em.find(classT,id);
        if (e == null) {
            return null;
        }
        return e;        
    }

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		System.out.println("AbstractDAO.getAll()");
		if (em == null) System.out.println("AbstractDAO: em is null");
		Query query = em.createNamedQuery(classT.getSimpleName() + ".findAll");
		return (List<T>) query.getResultList(); 
	}

	public boolean save(final T ent) {
		em.persist(ent);
		em.flush();
		return true;
	}	
	
	public boolean update(final int id, final T ent) {
		final T e =  em.find(classT,id);
		if (e == null) return false;
		em.merge(ent);
		em.flush();
		return true;
	}
	
	public boolean delete(final int id) {
		final T e =  em.find(classT,id);
		if (e == null) return false;
		em.remove(e);
		//em.flush();
		return true;
	}
	
	public boolean deleteAll() {
		Query query = em.createQuery("Delete from " + classT.getSimpleName());
		query.executeUpdate();
		return true;
	}	
}
