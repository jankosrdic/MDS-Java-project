package com.mycompany.mds.controller;

import com.mycompany.mds.model.database.dao.StockDAO;
import com.mycompany.mds.model.database.entity.Stock;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/stocks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockController {

    @Inject
    private StockDAO stockDAO;

    @GET
    public List<Stock> getAll() {
        return stockDAO.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Stock stock = stockDAO.findById(id);
        if (stock == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Stock not found with id: " + id).build();
        }
        return Response.ok(stock).build();
    }

    @POST
    public Response create(Stock stock) {
        stockDAO.save(stock);
        return Response.status(Response.Status.CREATED).entity(stock).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Stock updatedStock) {
        Stock existingStock = stockDAO.findById(id);
        if (existingStock == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Stock not found with id: " + id).build();
        }
        existingStock.setCompanyName(updatedStock.getCompanyName());
        existingStock.setTickerSymbol(updatedStock.getTickerSymbol());
        existingStock.setDateFounded(updatedStock.getDateFounded());
        existingStock.setIndustry(updatedStock.getIndustry());
        stockDAO.update(existingStock);
        return Response.ok(existingStock).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Stock stock = stockDAO.findById(id);
        if (stock == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Stock not found with id: " + id).build();
        }
        stockDAO.delete(id);
        return Response.noContent().build();
    }
}
