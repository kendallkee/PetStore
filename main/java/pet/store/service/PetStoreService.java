package pet.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

//Added class level annotation for @Service from the springframework
@Service
public class PetStoreService {
	
	//Created a PetStoreDao object with an Autowired annotation so Spring can inject the DAO object. 
	//Also had to import the pet.store.dao
	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired 
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;

	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		
		PetStore petStore;
		if(petStoreId == null) {
			petStore = findOrCreatePetStore(petStoreId);
		} else {
			petStore = findPetStoreById(petStoreId);
			if(petStore == null) {
				throw new NoSuchElementException("Pet store with ID " + petStoreId + " doesn't exist.");
			}
		}
		
		copyPetStoreFields(petStore, petStoreData);
		
		PetStore savedPetStore = petStoreDao.save(petStore);
		return new PetStoreData(savedPetStore);
	}
	
	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
			}
		return petStore;
		}
	
	
	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(() -> new NoSuchElementException("Pet store with ID:" + petStoreId + " does not exist."));
	
	}
		
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		
	}
	/*
	 * @Transactional(readOnly = false) public PetStoreEmployee saveEmployee(Long
	 * petStoreId, PetStoreEmployee petStoreEmployee) { PetStore petStore =
	 * petStoreDao.findById(petStoreId) .orElseThrow(() -> new
	 * NoSuchElementException("PetStore not found."));
	 * 
	 * Long employeeId = petStoreEmployee.getEmployeeId(); Employee employee =
	 * findOrCreateEmployee(petStoreId, employeeId);
	 * 
	 * copyEmployeeFields(employee, petStoreEmployee);
	 * 
	 * employee.setPetStore(petStore); petStore.getEmployees().add(employee);
	 * 
	 * Employee dbEmployee = employeeDao.save(employee);
	 * 
	 * return new PetStoreEmployee(dbEmployee); }
	 * 
	 * private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) { if
	 * (employeeId == null) { return new Employee(); } else { return
	 * findEmployeeById(petStoreId, employeeId); } }
	 * 
	 * private Employee findEmployeeById(Long petStoreId, Long employeeId) {
	 * Employee employee = employeeDao.findById(employeeId) .orElseThrow(() -> new
	 * NoSuchElementException("Employee not found"));
	 * 
	 * if (!Objects.equals(employee.getPetStore().getEmployeeId() , petStoreId)) {
	 * throw new
	 * IllegalArgumentException("Employee does not belong to the specified PetStore"
	 * ); }
	 * 
	 * return employee; }
	 * 
	 * private void copyEmployeeFields(Employee employee, PetStoreEmployee
	 * petStoreEmployee) {
	 * employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
	 * employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
	 * employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	 * employee.setEmployeePhone(petStoreEmployee.getEmployeePhone()); }
	 */
	
	@Transactional(readOnly = false)
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
        PetStore petStore = findPetStoreById(petStoreId);
        Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());
        
        copyEmployeeFields(employee, petStoreEmployee);
        
        employee.setPetStore(petStore);
        petStore.getEmployees().add(employee);
        
        Employee savedEmployee = employeeDao.save(employee);
        
        return new PetStoreEmployee(savedEmployee);
    }
	
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
        if (employeeId == null) {
            return new Employee();
        } else {
            return findEmployeeById(petStoreId, employeeId);
        }
    }

    private Employee findEmployeeById(Long petStoreId, Long employeeId) {
        Employee employee = employeeDao.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        
        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee does not belong to the specified PetStore");
        }
        
        return employee;
    }

    

    public void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
    	employee.setEmployeeId(petStoreEmployee.getEmployeeId());
        employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
        employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
        employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
        employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
    }

    @Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		Customer savedCustomer = customerDao.save(customer);
		
		return new PetStoreCustomer(savedCustomer);
		}
    
    public Customer findCustomerById(Long petStoreId, Long customerId) {
    	Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementException("Customer not found."));
    	
    	boolean isCustomerOfPetStore = customer.getPetStores().stream().anyMatch(store -> store.getPetStoreId().equals(petStoreId));
    	if(!isCustomerOfPetStore) {
    		throw new IllegalArgumentException("Customer does not belong to the specified store.");
    	}
    	return customer;
    }
    
    public Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
    	if(customerId == null) {
    		return new Customer();
    	} else {
    		return findCustomerById(petStoreId, customerId);
    	}
    }
    
    public void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
    	customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		
    }
    
    @Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		return petStores.stream().map(this::convertToPetStoreData).collect(Collectors.toList());
	}
    
    private PetStoreData convertToPetStoreData(PetStore petStore) {
    	PetStoreData petStoreData = new PetStoreData();
    	
    	petStoreData.setPetStoreId(petStore.getPetStoreId());
    	petStoreData.setPetStoreName(petStore.getPetStoreName());
    	petStoreData.setPetStoreAddress(petStore.getPetStoreAddress());
    	petStoreData.setPetStoreCity(petStore.getPetStoreCity());
    	petStoreData.setPetStoreState(petStore.getPetStoreState());
    	petStoreData.setPetStoreZip(petStore.getPetStoreZip());
    	petStoreData.setPetStorePhone(petStore.getPetStorePhone());
    	
    	return petStoreData;
    }

    @Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = petStoreDao.findById(petStoreId).orElseThrow(() -> new NoSuchElementException("Pet Store not found."));
		
		return convertToPetStoreDataById(petStore);
	}
    
    private PetStoreData convertToPetStoreDataById(PetStore petStore) {
    	PetStoreData petStoreData = new PetStoreData();
    	petStoreData.setPetStoreId(petStore.getPetStoreId());
    	petStoreData.setPetStoreName(petStore.getPetStoreName());
    	petStoreData.setPetStoreAddress(petStore.getPetStoreAddress());
    	petStoreData.setPetStoreCity(petStore.getPetStoreCity());
    	petStoreData.setPetStoreState(petStore.getPetStoreState());
    	petStoreData.setPetStoreZip(petStore.getPetStoreZip());
    	petStoreData.setPetStorePhone(petStore.getPetStorePhone());
    	petStoreData.setCustomers(petStore.getCustomers().stream().map(PetStoreCustomer::new).collect(Collectors.toSet()));
        petStoreData.setEmployees(petStore.getEmployees().stream().map(PetStoreEmployee::new).collect(Collectors.toSet()));
        
    	return petStoreData;
    }

    @Transactional
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = petStoreDao.findById(petStoreId).orElseThrow(() -> new NoSuchElementException("Pet Store not found."));
		
		petStoreDao.delete(petStore);
		
	}

	
}
